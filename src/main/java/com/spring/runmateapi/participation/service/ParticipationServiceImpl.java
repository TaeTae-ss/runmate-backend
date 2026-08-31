package com.spring.runmateapi.participation.service;


import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.participation.dto.ParticipationDTO;
import com.spring.runmateapi.participation.entity.Participation;
import com.spring.runmateapi.participation.mapper.ParticipationMapper;
import com.spring.runmateapi.participation.repository.ParticipationRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final RunningRepository runningRepository;
    private final ParticipationMapper participationMapper;
    private final MemberRepository memberRepository;

    private Running getRunning(Long runningId) {
        return runningRepository.findById(runningId)
                .orElseThrow(() ->
                        new EntityNotFoundException(runningId + "번 모임이 존재하지 않습니다."));
    }

    // 신청로직
    @Override
    public Long apply(ParticipationDTO participationDTO) {
        Long runningId = participationDTO.getRunningId();
        Long memberId = participationDTO.getMemberId();

        Running running = getRunning(runningId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(memberId + "번 회원이 존재하지 않습니다."));

        //중복 참가 방지
        if (participationRepository.existsByRunning_RunningIdAndMember_MemberId(runningId, memberId)) {
            throw new IllegalStateException("이미 신청한 모임입니다.");
        }
        // 모집 마감 여부 체크
        if (!running.isStatus()) {
            throw new IllegalStateException("이미 마감된 모임입니다.");
        }
        // 참가 인원 제한 체크 (신청 시점에 이미 다 찼는지)
        long currentCount = participationRepository.countByRunning_RunningId(runningId);
        if (currentCount >= running.getMaxPeople()) {
            throw new IllegalStateException("모집 인원이 마감되었습니다.");
        }
        // 신청 처리
        Participation participation = Participation.builder()
                .running(running)
                .member(member)
                .build();
        Participation saved = participationRepository.save(participation);

        // 신청 후 인원이 다 찼으면 자동 마감
        long updateCount = currentCount + 1;
        if (updateCount >= running.getMaxPeople()) {
            running.updateStatus(false);
        }
        return saved.getParticipationId();
    }

    // 신청취소 로직
    @Override
    public void cancel(Long runningId, Long memberId) {
        if (!participationRepository.existsByRunning_RunningIdAndMember_MemberId(runningId, memberId)) {
            throw new IllegalStateException("신청 내역이 존재하지 않습니다.");
        }
        participationRepository.deleteByRunning_RunningIdAndMember_MemberId(runningId, memberId);

        Running running = getRunning(runningId);
        if (!running.isStatus()) { // 모집마감 상태였으면
            long currentCount = participationRepository.countByRunning_RunningId(runningId);
            if (currentCount < running.getMaxPeople()) {
                running.updateStatus(true); // 빈자리가 생겼으니 다시 모집중으로
            }
        }
    }

    // 모임별 참가자 조회
    @Override
    @Transactional(readOnly = true)
    public List<ParticipationDTO> getListByRunning(Long runningId) {
        return participationRepository.findByRunning_RunningId(runningId)
                .stream()
                .map(participationMapper::toDTO)
                .toList();
    }

    // 회원별 참가 목록 조회
    @Override
    public List<ParticipationDTO> getListByMember(Long memberId) {
        return participationRepository.findByMember_MemberId(memberId)
                .stream()
                .map(participationMapper::toDTO)
                .toList();

    }
}
