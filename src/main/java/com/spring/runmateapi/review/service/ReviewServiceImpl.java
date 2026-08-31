package com.spring.runmateapi.review.service;

import com.spring.runmateapi.review.dto.ReviewDTO;
import com.spring.runmateapi.review.entity.Review;
import com.spring.runmateapi.review.mapper.ReviewMapper;
import com.spring.runmateapi.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public Long register(ReviewDTO reviewDTO) {

        Review review = new Review(
                reviewDTO.getRating(),
                reviewDTO.getContent(),
                reviewDTO.getMemberId(),
                reviewDTO.getRunningId()
        );

        Review savedReview = reviewRepository.save(review);

        return savedReview.getReviewId();
    }

    @Override
    public List<ReviewDTO> getList(Long runningId) {

        List<Review> reviewList =
                reviewRepository.findByRunningId(runningId);

        return reviewList.stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Override
    public ReviewDTO get(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 후기입니다."));

        return reviewMapper.toDTO(review);
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getReviewId())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 후기입니다."));

        review.changeReview(reviewDTO.getContent());
    }

    @Override
    public void remove(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 후기입니다."));

        reviewRepository.delete(review);
    }
}