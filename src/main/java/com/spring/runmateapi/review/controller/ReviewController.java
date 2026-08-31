package com.spring.runmateapi.review.controller;

import com.spring.runmateapi.review.dto.ReviewDTO;
import com.spring.runmateapi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/runmate/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 후기 등록
    @PostMapping
    public ResponseEntity<Long> register(@RequestBody ReviewDTO reviewDTO) {

        Long reviewId = reviewService.register(reviewDTO);

        return ResponseEntity.ok(reviewId);
    }

    // 특정 러닝 모임의 후기 목록 조회
    @GetMapping("/running/{runningId}")
    public ResponseEntity<List<ReviewDTO>> getList(
            @PathVariable Long runningId) {

        List<ReviewDTO> reviewList =
                reviewService.getList(runningId);

        return ResponseEntity.ok(reviewList);
    }

    // 후기 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> get(
            @PathVariable Long reviewId) {

        ReviewDTO reviewDTO =
                reviewService.get(reviewId);

        return ResponseEntity.ok(reviewDTO);
    }

    // 후기 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> modify(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO) {

        reviewDTO.setReviewId(reviewId);

        reviewService.modify(reviewDTO);

        return ResponseEntity.ok().build();
    }

    // 후기 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long reviewId) {

        reviewService.remove(reviewId);

        return ResponseEntity.ok().build();
    }
}
