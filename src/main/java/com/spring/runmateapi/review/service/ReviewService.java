package com.spring.runmateapi.review.service;

import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.review.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    Long register(ReviewDTO reviewDTO);

    // 특정 러닝 모임의 후기 목록 조회
    PageResponseDTO<ReviewDTO> getList(Long runningId, PageRequestDTO pageRequestDTO);

    // 후기 상세 조회
    ReviewDTO get(Long reviewId);

    void modify(ReviewDTO reviewDTO);

    void remove(Long reviewId);
}
