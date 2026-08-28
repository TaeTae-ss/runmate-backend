package com.spring.runmateapi.review.service;

import com.spring.runmateapi.review.mapper.ReviewMapper;
import com.spring.runmateapi.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;


}
