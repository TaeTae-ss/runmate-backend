package com.spring.runmateapi.participation.repository;

import com.spring.runmateapi.participation.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByRunningIdAndMemberId(Long runningId, Long memberId);
    long countByRunningId(Long runningId);
    List<Participation> findByRunningId(Long runningId);
    List<Participation> findByMemberId(Long memberId);
    void deleteByRunningIdAndMemberId(Long runningId, Long memberId);
}
