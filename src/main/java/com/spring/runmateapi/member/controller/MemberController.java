package com.spring.runmateapi.member.controller;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.member.dto.LoginDTO;
import com.spring.runmateapi.member.dto.MemberDTO;
import com.spring.runmateapi.member.dto.TokenRequestDTO;
import com.spring.runmateapi.member.service.MemberService;
import com.spring.runmateapi.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/runmate/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @GetMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDTO> get(@PathVariable(name = "memberId") Long memberId) {
        MemberDTO memberDTO = memberService.get(memberId);

        return ResponseEntity.ok(memberDTO);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponseDTO<MemberDTO>> getList(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<MemberDTO> response = memberService.getList(pageRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(hidden = true)
    public ResponseEntity<Map<String, Long>> register(@RequestBody MemberDTO memberDTO) {
        Long memberId = memberService.register(memberDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(Map.of("memberId", memberId));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> modify(@PathVariable(name = "memberId") Long memberId, @RequestBody MemberDTO memberDTO) {
        memberDTO.setMemberId(memberId);
        memberService.modify(memberDTO);
        return  ResponseEntity.ok(Map.of("result", "success"));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable(name = "memberId") Long memberId) {
        memberService.remove(memberId);
        return ResponseEntity.ok(Map.of("result","success"));
    }

    @PostMapping("/login")
    @Operation(hidden = true)
    public Map<String, Object> login(@RequestBody LoginDTO loginDTO) {
        // 인증 전 상태의 인증 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUserId(), loginDTO.getPassword());

        // Spring Security에 인증 요청
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 인증된 사용자 정보 가져오기
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

        // JWT에 저장할 회원 정보
        Map<String, Object> claims = memberDTO.getClaims();
        // Access Token: 10분
        String accessToken = JWTUtil.generateToken(claims, 10);
        // Refresh Token: 24시간
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24);

        // 응답 데이터 생성
        Map<String, Object> result = new HashMap<>(claims);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody TokenRequestDTO tokenRequestDTO) {
        String accessToken = tokenRequestDTO.getAccessToken();
        String refreshToken = tokenRequestDTO.getRefreshToken();

        // Access Token이 아직 유효하면 기존 토큰을 그대로 반환
        try {
            JWTUtil.validateToken(accessToken);
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        } catch (ExpiredJwtException e) {
            // Access Token이 만료된 경우 Refresh Token을 확인한다.
        }

        // Refresh Token 검증
        Map<String, Object> refreshClaims = JWTUtil.validateToken(refreshToken);

        // 새로운 JWT에 저장할 회원 정보
        Map<String, Object> claims = Map.of(
                "email", refreshClaims.get("email"),
                "nickname", refreshClaims.get("nickname"),
                "social", refreshClaims.get("social"),
                "roleNames", refreshClaims.get("roleNames")
        );

        // 새로운 Access Token 생성 - 10분
        String newAccessToken = JWTUtil.generateToken(claims, 10);

        // 기본적으로 기존 Refresh Token을 사용
        String newRefreshToken = refreshToken;

        // Refresh Token의 만료 시간
        long expiration = ((Number) refreshClaims.get("exp")).longValue() * 1000L;

        // Refresh Token의 남은 유효시간
        long remainingTime = expiration - System.currentTimeMillis();

        // 남은 시간이 1시간 미만이면 Refresh Token도 새로 생성
        if (remainingTime < 1000L * 60 * 60) {
            newRefreshToken = JWTUtil.generateToken(claims, 60 * 24);
        }

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}
