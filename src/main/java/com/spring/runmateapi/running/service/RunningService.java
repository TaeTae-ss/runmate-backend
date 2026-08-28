package com.spring.runmateapi.running.service;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.running.dto.RunningDTO;

public interface RunningService {
    Long register(RunningDTO runningDTO);
    RunningDTO get(Long runningId);
    void modify(RunningDTO runningDTO);
    void remove(Long runningId);
    void updateStatus(Long runningId, boolean status); //스테이터스 변경 로직

    // 페이징 처리
    PageResponseDTO<RunningDTO> list(PageRequestDTO pageRequestDTO);
}
