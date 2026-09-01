package com.spring.runmateapi.review.repository;

import com.spring.runmateapi.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRunning_RunningId(Long runningId, Pageable pageable);

    // 회원이 작성한 후기 전체 삭제
    void deleteByMember_MemberId(Long memberId);

    // 모임 삭제 시 해당 모임의 후기 전체 삭제
    void deleteByRunning_RunningId(Long runningId);
}
