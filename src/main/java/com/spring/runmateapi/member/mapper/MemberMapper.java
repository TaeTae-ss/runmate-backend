package com.spring.runmateapi.member.mapper;

import com.spring.runmateapi.member.dto.MemberDTO;
import com.spring.runmateapi.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberDTO toDTO(Member member);
}
