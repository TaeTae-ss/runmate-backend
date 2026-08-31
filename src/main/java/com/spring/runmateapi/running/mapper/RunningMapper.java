package com.spring.runmateapi.running.mapper;

import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.entity.Running;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RunningMapper {
    @Mapping(source = "member.memberId", target = "memberId")
    RunningDTO toDTO(Running running);
}
