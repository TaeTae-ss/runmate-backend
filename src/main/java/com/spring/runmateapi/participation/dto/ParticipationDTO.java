package com.spring.runmateapi.participation.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ParticipationDTO {
    private Long participationId;
    private Long runningId;
    private Long memberId;
    private LocalDate joinedAt;
    private String memberNickname; //멤버 닉네임 추가
}
