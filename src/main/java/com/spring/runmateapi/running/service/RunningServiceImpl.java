package com.spring.runmateapi.running.service;


import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.participation.entity.Participation;
import com.spring.runmateapi.participation.repository.ParticipationRepository;
import com.spring.runmateapi.review.repository.ReviewRepository;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.mapper.RunningMapper;
import com.spring.runmateapi.running.repository.RunningRepository;
import com.spring.runmateapi.runningLike.repository.RunningLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RunningServiceImpl implements RunningService {

    private final RunningRepository runningRepository;
    private final RunningMapper runningMapper;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ParticipationRepository participationRepository;
    private final RunningLikeRepository runningLikeRepository;

    @Override
    public Long register(RunningDTO runningDTO) {
        Member member = memberRepository.findById(runningDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(runningDTO.getMemberId() + "번 회원이 존재하지 않습니다."));

        //생성자를 이용하여 DTO -> entity로 변환
        Running running = Running.builder()
                .title(runningDTO.getTitle())
                .location(runningDTO.getLocation())
                .place(runningDTO.getPlace())
                .latitude(runningDTO.getLatitude()) //위도
                .longitude(runningDTO.getLongitude()) //경도
                .runDate(runningDTO.getRunDate())
                .startTime(runningDTO.getStartTime())
                .runTime(runningDTO.getRunTime())
                .distance(runningDTO.getDistance())
                .maxPeople(runningDTO.getMaxPeople())
                .content(runningDTO.getContent())
                .status(runningDTO.isStatus())
                .member(member)
                .build();
        Running savedRunning = runningRepository.save(running);

        //모임장을 자동으로 참가자에 등록
        Participation participation = Participation.builder()
                .running(savedRunning)
                .member(member)
                .build();
        participationRepository.save(participation);

        //등록 직후 정원 체크 (maxPeople=1인 경우 대비)
        long currentCount = participationRepository.countByRunning_RunningId(savedRunning.getRunningId());
        if(currentCount >= savedRunning.getMaxPeople()) {
            savedRunning.updateStatus(false);
        }

        return savedRunning.getRunningId();
    }

    private Running getRunning(Long runningId) {
        return runningRepository.findById(runningId)
                .orElseThrow(() ->
                        new EntityNotFoundException(runningId + "번 모임이 존재하지 않습니다."));
    }

    @Override
    public RunningDTO get(Long runningId) {
        Running running = getRunning(runningId);
        RunningDTO runningDTO = runningMapper.toDTO(running); //entity에서 -> DTO 변환
        runningDTO.setStatus(calculateActualStatus(running)); // 실제 상태로 덮어쓰기
        return  runningDTO;
    }

    //수정
    @Override
    public void modify(RunningDTO runningDTO) {
        Running running = getRunning(runningDTO.getRunningId());
        running.changeRunning(
                runningDTO.getTitle(),
                runningDTO.getLocation(),
                runningDTO.getPlace(),
                runningDTO.getLatitude(),
                runningDTO.getLongitude(),
                runningDTO.getRunDate(),
                runningDTO.getStartTime(),
                runningDTO.getRunTime(),
                runningDTO.getDistance(),
                runningDTO.getMaxPeople(),
                runningDTO.getContent(),
                runningDTO.isStatus()
        );
    }

    //삭제
    @Override
    @Transactional
    public void remove(Long runningId) {
        Running running = getRunning(runningId);
        reviewRepository.deleteByRunning_RunningId(runningId);
        participationRepository.deleteByRunning_RunningId(runningId);
        runningRepository.delete(running);
    }

    //시작시간이 지났는지 계산해서 길제 상태를 판다
    private boolean calculateActualStatus(Running running) {
        if(!running.isStatus()) {
            return false; // 이미 모임장이 마감시킨 건 그대로 마감유지
        }
        LocalDateTime runDateTime = LocalDateTime.of(
                running.getRunDate(),
                LocalTime.parse(running.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))
        );
        return LocalDateTime.now().isBefore(runDateTime); // 시작시간 전이면 true(모집중), 지났으면 false(모집 마감)
    }


    //페이징 처리
    @Transactional(readOnly = true)
    @Override
    public PageResponseDTO<RunningDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("runningId");
        Page<Running> runningPage = runningRepository.searchList(pageRequestDTO, pageable);

        List<RunningDTO> runningDTOList = runningPage.getContent()
                .stream()
                .map(r -> {
                    RunningDTO dto = runningMapper.toDTO(r);
                    dto.setStatus(calculateActualStatus(r));
                    dto.setLikeCount(runningLikeRepository.countByRunning_RunningId(r.getRunningId())); //목록에 똑같이 status값이 반환되게 설정
                    return  dto;
                })
                .toList();
        return new PageResponseDTO<>(runningDTOList, pageRequestDTO, runningPage.getTotalElements());
    }

    // status변경 처리
    @Override
    public void updateStatus(Long runningId, boolean status) {
        Running running = getRunning(runningId);
        running.updateStatus(status);
    }
}
