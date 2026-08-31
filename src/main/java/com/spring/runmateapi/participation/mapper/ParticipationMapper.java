package com.spring.runmateapi.participation.mapper;


import com.spring.runmateapi.participation.dto.ParticipationDTO;
import com.spring.runmateapi.participation.entity.Participation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {
    ParticipationDTO toDTO(Participation participation);
}
