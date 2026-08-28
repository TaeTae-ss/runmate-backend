package com.spring.runmateapi.config;

import com.spring.runmateapi.security.filter.JWTCheckFilter;
import com.spring.runmateapi.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
//@EnableMethodSecurity
public class CustomSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration ) throws  Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 정책을 Spring Security에 적용
                .cors(cors ->
                        cors.configurationSource((corsConfigurationSource()))
                )
                // JWT 기반 API 서버에서는 CSRF 보호 기능을 사용하지 않는다.
                .csrf(csrf -> csrf.disable())
                // 서버에 로그인 상태를 저장하지 않는 Stateless 방식으로 설정한다.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(form -> form.disable())
                // 현재 모든 요청을 허용
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );
                // 접근 권한이 부족한 경우 실행할 Handler 설정
//                .exceptionHandling(exception ->
//                        exception.accessDeniedHandler(new CustomAccessDeniedHandler())
//                )
//                // JWTCheckFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
//                .addFilterBefore(
//                        // Access Token을 검사할 사용자 정의 필터 객체
//                        new JWTCheckFilter(),
//                        // JWTCheckFilter가 이 필터보다 먼저 실행되도록 기준 위치를 지정
//                        UsernamePasswordAuthenticationFilter.class
//                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 출처의 요청을 허용
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 허용할 HTTP 메서드 목록을 설정
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );
        // 요청에서 허용할 HTTP 헤더 목록을 설정
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Cache-Control", "Content-Type")
        );
        // 쿠키 등의 자격 증명을 포함한 교차 출처 요청을 허용
        configuration.setAllowCredentials(true);
        // URL 패턴별로 CORS 설정을 등록하기 위한 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 요청 경로에 CORS 정책 적용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}