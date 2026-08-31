package com.spring.runmateapi.review.repository;

import com.spring.runmateapi.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRunning_RunningId(Long runningId, Pageable pageable);
}
