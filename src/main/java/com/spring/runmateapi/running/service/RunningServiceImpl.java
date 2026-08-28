package com.spring.runmateapi.running.service;


import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.mapper.RunningMapper;
import com.spring.runmateapi.running.repogitory.RunningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Override
    public Long register(RunningDTO runningDTO) {
        //생성자를 이용하여 DTO -> entity로 변환
        Running running = Running.builder()
                .title(runningDTO.getTitle())
                .location(runningDTO.getLocation())
                .place(runningDTO.getPlace())
                .runDate(runningDTO.getRunDate())
                .startTime(runningDTO.getStartTime())
                .runTime(runningDTO.getRunTime())
                .distance(runningDTO.getDistance())
                .maxPeople(runningDTO.getMaxPeople())
                .content(runningDTO.getContent())
                .status(runningDTO.isStatus())
                .memberId(runningDTO.getMemberId())
                .build();
        Running savedRunning = runningRepository.save(running);
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
    public void remove(Long runningId) {
        Running running = getRunning(runningId);
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
                .map(running -> {
                    RunningDTO runningDTO = runningMapper.toDTO(running);
                    runningDTO.setStatus(calculateActualStatus(running)); //목록에 똑같이 status값이 반환되게 설정
                    return  runningDTO;
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
