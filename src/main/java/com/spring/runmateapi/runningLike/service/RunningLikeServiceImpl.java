package com.spring.runmateapi.runningLike.service;


import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import com.spring.runmateapi.runningLike.entity.RunningLike;
import com.spring.runmateapi.runningLike.repository.RunningLikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RunningLikeServiceImpl implements RunningLikeService {

    private final RunningLikeRepository runningLikeRepository;
    private final RunningRepository runningRepository;
    private final MemberRepository memberRepository;

    @Override
    public void toggleLike(Long runningId, Long memberId) {
        boolean alreadyLiked = runningLikeRepository
                .existsByRunning_RunningIdAndMember_MemberId(runningId, memberId);

        if(alreadyLiked) {
            //이미 좋아요가 눌려있으면 취소
            runningLikeRepository.deleteByRunning_RunningIdAndMember_MemberId(runningId, memberId);
        } else {
            // 좋아요 등록
            Running running = runningRepository.findById(runningId)
                    .orElseThrow(() -> new EntityNotFoundException(runningId + "번 모임이 존재하지 않습니다."));
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new EntityNotFoundException(memberId + "번 회원이 존재하지 않습니다."));

            RunningLike like = RunningLike.builder()
                    .running(running)
                    .member(member)
                    .build();
            runningLikeRepository.save(like);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long runningId) {
        return runningLikeRepository.countByRunning_RunningId(runningId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(Long runningId, Long memberId) {
        return runningLikeRepository.existsByRunning_RunningIdAndMember_MemberId(runningId, memberId);
    }
}
