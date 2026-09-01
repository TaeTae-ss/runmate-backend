package com.spring.runmateapi.member.mapper;

import com.spring.runmateapi.member.dto.MemberDTO;
import com.spring.runmateapi.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    @Mapping(
            target = "roleNames",
            expression = "java(member.getRoles().stream().map(role -> role.name()).toList())"
    )
    MemberDTO toDTO(Member member);
}
