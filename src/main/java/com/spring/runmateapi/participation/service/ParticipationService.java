package com.spring.runmateapi.participation.service;

import com.spring.runmateapi.participation.dto.ParticipationDTO;

import java.util.List;

public interface ParticipationService {
    Long apply(ParticipationDTO participationDTO); //참가신청
    void cancel(Long runningId, Long memberId); //신청취소
    List<ParticipationDTO> getListByRunning(Long runningId); //모임별 참가자 조회
    List<ParticipationDTO> getListByMember(Long memberId); //회원별 참가 목록 조회

}
