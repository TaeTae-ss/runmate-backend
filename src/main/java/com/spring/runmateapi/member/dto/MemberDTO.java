package com.spring.runmateapi.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter

public class MemberDTO extends User {
    private Long memberId;
    private String userId;
    private String email;
    private String password;
    private String nickname;
    private String phone;

    // JSON 변환 시 createAt를 "yyy-MM-dd" 형식의 문자열로 표현한다.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    private List<String> roleNames;

    public MemberDTO(Long memberId, String userId,String password, String nickname, String email, String phone, LocalDate createdAt, List<String> roleNames) {
        // 권한 이름에 "ROLE_" 접두어를 붙인 뒤
        // Spring Security가 사용하는 SimpleGrantedAuthority 객체로 변환한다.
        super(userId,password,
                roleNames == null
                        ? List.of()
                        : roleNames.stream()
                          .map(role ->
                               new SimpleGrantedAuthority("ROLE_" + role))
                          .toList()
        );
        this.memberId = memberId;
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("memberId",memberId);
        dataMap.put("userId",userId);
        dataMap.put("nickname",nickname);
        dataMap.put("email",email);
        dataMap.put("roleNames",roleNames);

        return dataMap;
    }


}
