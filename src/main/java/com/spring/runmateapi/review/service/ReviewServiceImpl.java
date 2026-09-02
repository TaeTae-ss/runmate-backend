package com.spring.runmateapi.review.service;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.participation.repository.ParticipationRepository;
import com.spring.runmateapi.review.dto.ReviewDTO;
import com.spring.runmateapi.review.entity.Review;
import com.spring.runmateapi.review.mapper.ReviewMapper;
import com.spring.runmateapi.review.repository.ReviewRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final MemberRepository memberRepository;
    private final RunningRepository runningRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public Long register(ReviewDTO reviewDTO) {

        Member member = memberRepository.findById(reviewDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(reviewDTO.getMemberId() + "번 회원이 존재하지 않습니다."));

        Running running = runningRepository.findById(reviewDTO.getRunningId())
                .orElseThrow(() -> new EntityNotFoundException(reviewDTO.getRunningId() + "번 모임이 존재하지 않습니다."));

        // 참여자 검증
        boolean isParticipation = participationRepository
                .existsByRunning_RunningIdAndMember_MemberId(
                        reviewDTO.getRunningId(),
                        reviewDTO.getMemberId()
                );
        if(!isParticipation) {
            throw new IllegalStateException("참여한 모임만 후기를 작성할 수 있습니다.");
        }

        // 러닝 날짜가 지났는지 검증 (추가)
        LocalDateTime runDateTime = LocalDateTime.of(
                running.getRunDate(),
                LocalTime.parse(running.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))
        );
        if(LocalDateTime.now().isBefore(runDateTime)) {
            throw new IllegalStateException("모임이 끝나 후에 후기를 작성할 수 있습니다.");
        }

        Review review = new Review(
                reviewDTO.getRating(),
                reviewDTO.getContent(),
                member, // Long memberId -> Member 객체
                running // Long runningId = Running 객체
        );

        Review savedReview = reviewRepository.save(review);
        return savedReview.getReviewId();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ReviewDTO> getList(Long runningId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("createdAt");

        Page<Review> reviewPage =
                reviewRepository.findByRunning_RunningId(runningId, pageable);

        List<ReviewDTO> dtoList = reviewPage.getContent()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();

        return new PageResponseDTO<ReviewDTO>(dtoList, pageRequestDTO, reviewPage.getTotalElements());
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