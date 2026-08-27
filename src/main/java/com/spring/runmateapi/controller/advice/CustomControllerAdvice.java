package com.spring.runmateapi.controller.advice;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg",e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg",e.getMessage()));
    }

    // 401 Unauthorized는 요청한 사용자의 인증 정보가 없거나 올바르지 않아 인증에 실패한 경우 반환하는 HTTP 상태 코드
    // 로그인 인증 실패 처리
//    @ExceptionHandler(AuthenticationException.class)
//    protected  ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of("msg","아이디 또는 비밀번호가 올바르지 않습니다."));
//    }
//
//    @ExceptionHandler(JwtException.class)
//    protected ResponseEntity<?> handleJwtException(JwtException e) {
//        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of("msg","유효하지 않은 토큰입니다."));
//    }
}
