package com.spring.runmateapi.member.service;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.member.dto.MemberDTO;
import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.entity.MemberRole;
import com.spring.runmateapi.member.mapper.MemberMapper;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.participation.repository.ParticipationRepository;
import com.spring.runmateapi.review.repository.ReviewRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ParticipationRepository participationRepository;
    private final ReviewRepository reviewRepository;
    private final RunningRepository runningRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(MemberDTO memberDTO) {
        String encodedPassword =
                passwordEncoder.encode(memberDTO.getPassword());

        Member member = new Member(memberDTO.getUserId(), encodedPassword, memberDTO.getNickname(), memberDTO.getEmail(), memberDTO.getPhone());
        member.addRole(MemberRole.USER);
        Member savedMember = memberRepository.save(member);
        return savedMember.getMemberId();
    }

    @Override
    public boolean checkUserId(String userId) {
        return !memberRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkEmail(String email) {
        return !memberRepository.existsByEmail(email);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new EntityNotFoundException(memberId + "회원이 존재하지 않습니다."));
    }

    @Override
    public MemberDTO get(Long memberId) {
        Member member = getMember(memberId);
        return memberMapper.toDTO(member);
    }

    @Override
    @Transactional( readOnly=true )
    public PageResponseDTO<MemberDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("memberId");
        Page<Member> memberPage = memberRepository.findAll(pageable);

        List<MemberDTO> dtoList = memberPage.getContent()
                .stream()
                .map(memberMapper::toDTO)
                .toList();

        return new PageResponseDTO<>(
                dtoList,
                pageRequestDTO,
                memberPage.getTotalElements()
        );
    }

    @Override
    public void modify(MemberDTO memberDTO) {
        Member member = getMember(memberDTO.getMemberId());
        member.changeNickname(memberDTO.getNickname());
        if (memberDTO.getPassword() != null &&
                !memberDTO.getPassword().isBlank()) {
            member.changePw(passwordEncoder.encode(memberDTO.getPassword()));
        }
        member.changePhone(memberDTO.getPhone());
    }

    @Override
    public void remove(Long memberId) {
        // 회원 존재 여부 확인
        Member member = getMember(memberId);

        // 1. 회원이 작성한 후기 삭제
        reviewRepository.deleteByMember_MemberId(memberId);

        // 2. 회원의 참여 데이터 삭제
        participationRepository.deleteByMember_MemberId(memberId);

        // 3. 회원이 대표자인 모임 조회
        List<Running> runningList =
                runningRepository.findByMember_MemberId(memberId);

        // 4. 회원이 만든 모임 삭제
        for (Running running : runningList) {

            Long runningId = running.getRunningId();

            // 해당 모임의 후기 삭제
            reviewRepository.deleteByRunning_RunningId(runningId);

            // 해당 모임의 참여 데이터 삭제
            participationRepository.deleteByRunning_RunningId(runningId);

            // 모임 삭제
            runningRepository.delete(running);
        }

        // 5. 회원 삭제
        memberRepository.delete(member);
    }
}
