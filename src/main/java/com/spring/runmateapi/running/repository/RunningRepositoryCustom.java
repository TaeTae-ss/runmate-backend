package com.spring.runmateapi.running.repository;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.running.entity.Running;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RunningRepositoryCustom {
    Page<Running> searchList(PageRequestDTO pageRequestDTO, Pageable pageable);
}
