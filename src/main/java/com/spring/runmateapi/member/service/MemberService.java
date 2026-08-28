package com.spring.runmateapi.member.service;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.member.dto.MemberDTO;

public interface MemberService {
    Long register(MemberDTO memberDTO);
    MemberDTO get(Long memberId);
    PageResponseDTO<MemberDTO> getList(PageRequestDTO pageRequestDTO);
    void modify(MemberDTO memberDTO);
    void remove(Long memberId);
}
