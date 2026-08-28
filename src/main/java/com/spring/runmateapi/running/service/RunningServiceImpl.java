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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return runningMapper.toDTO(running); //entity에서 -> DTO 변환
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

    //페이징 처리
    @Transactional(readOnly = true)

    @Override
    public PageResponseDTO<RunningDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("runningId");
        Page<Running> runningPage = runningRepository.findAll(pageable);

        List<RunningDTO> runningDTOList = runningPage.getContent()
                .stream()
                .map(runningMapper::toDTO)
                .toList();
        return new PageResponseDTO<>(runningDTOList, pageRequestDTO, runningPage.getTotalElements());
    }
}
