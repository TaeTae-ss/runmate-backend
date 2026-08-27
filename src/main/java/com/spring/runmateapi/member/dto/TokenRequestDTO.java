package com.spring.runmateapi.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDTO {
    private String accessToken;
    private String refreshToken;
}
