package com.spring.runmateapi.member.service;

import com.spring.runmateapi.member.dto.MemberDTO;

public interface MemberService {
    Long register(MemberDTO memberDTO);
    MemberDTO get(Long memberId);
    void modify(MemberDTO memberDTO);
    void remove(Long memberId);
}
