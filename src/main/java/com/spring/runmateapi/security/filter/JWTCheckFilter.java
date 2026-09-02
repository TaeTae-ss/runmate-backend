package com.spring.runmateapi.security.filter;

import com.spring.runmateapi.member.dto.MemberDTO;
import com.spring.runmateapi.util.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("JWTCheckFilter 실행");

        String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}", authorizationHeader);
        // Authorization 헤더가 없거나 Bearer 형식이 아니면 인증 실패
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(
                    """
                            {"msg":"Access Token이 없습니다."}
                            """
            );
            return;
        }

        // "Bearer " 제거
        String accessToken = authorizationHeader.substring(7);

        try {
            // JWT 검증
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);
            // JWT에서 회원 정보 가져오기
            Long memberId = ((Number) claims.get("memberId")).longValue();
            String userId = (String) claims.get("userId");
            String nickname = (String) claims.get("nickname");
            String email = (String) claims.get("email");

            // JWT에서 권한 목록 가져오기
            List<String> roleNames =
                    ((List<?>) claims.get("roleNames"))
                            .stream()
                            .map(Object::toString)
                            .toList();

            // MemberDTO 생성
            MemberDTO memberDTO = new MemberDTO(
                    memberId,
                    userId,
                    "",
                    nickname,
                    email,
                    "",
                    null,
                    roleNames
            );

            // 인증 객체 생성
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            memberDTO,
                            null,
                            memberDTO.getAuthorities()
                    );

            // SecurityContext에 인증 정보 저장
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);

        } catch (JwtException e) {

            log.error("JWT 검증 실패: {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(
                    """
                            {"msg":"유효하지 않은 Access Token입니다."}
                            """
            );
            return;
        }

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) throws ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        log.info("Check URI: {}", path);

        // CORS Preflight 요청 제외
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 회원가입 요청 제외
        if (path.equals("/runmate/members") && "POST".equalsIgnoreCase(method)) {
            return true;
        }

        // 로그인 요청 제외
        if (path.equals("/runmate/members/login")) {
            return true;
        }

        // 아이디 중복확인 요청 제외
        if (path.equals("/runmate/members/check-userId")
                && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        // 이메일 중복확인 요청 제외
        if (path.equals("/runmate/members/check-email")
                && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        // 모임 목록 제외
        if ("GET".equalsIgnoreCase(method) && path.startsWith("/runmate/runnings")) {
            return true;
        }

        // 리뷰 목록 제외
        if ("GET".equalsIgnoreCase(method)
                && path.startsWith("/runmate/reviews/running/")) {
            return true;
        }

        // Access Token 재발급 요청은 JWT 검사에서 제외
        if (path.equals("/runmate/members/refresh")) {
            return true;
        }

        return false;
    }
}