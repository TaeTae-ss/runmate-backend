package com.spring.runmateapi.participation.repository;

import com.spring.runmateapi.participation.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByRunning_RunningIdAndMember_MemberId(Long runningId, Long memberId);
    long countByRunning_RunningId(Long runningId);
    List<Participation> findByRunning_RunningId(Long runningId);
    List<Participation> findByMember_MemberId(Long memberId);
    void deleteByRunning_RunningIdAndMember_MemberId(Long runningId, Long memberId);
}
