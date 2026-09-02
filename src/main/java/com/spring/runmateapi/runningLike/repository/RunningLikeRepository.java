package com.spring.runmateapi.runningLike.repository;

import com.spring.runmateapi.runningLike.entity.RunningLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningLikeRepository extends JpaRepository<RunningLike, Long> {
    boolean existsByRunning_RunningIdAndMember_MemberId(Long runningId, Long memberId);
    long countByRunning_RunningId(Long runningId);
    void deleteByRunning_RunningIdAndMember_MemberId(Long runningId, Long memberId);
}
