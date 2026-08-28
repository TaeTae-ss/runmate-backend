package com.spring.runmateapi.running.mapper;

import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.entity.Running;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RunningMapper {
    RunningDTO toDTO(Running running);
}
