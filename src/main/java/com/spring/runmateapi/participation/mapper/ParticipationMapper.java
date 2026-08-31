package com.spring.runmateapi.participation.mapper;


import com.spring.runmateapi.participation.dto.ParticipationDTO;
import com.spring.runmateapi.participation.entity.Participation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {
    @Mapping(source = "running.runningId", target = "runningId")
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "member.nickname", target = "memberNickname")
    ParticipationDTO toDTO(Participation participation);
}
