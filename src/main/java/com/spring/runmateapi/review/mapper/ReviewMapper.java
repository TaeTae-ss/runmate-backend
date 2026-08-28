package com.spring.runmateapi.review.mapper;

import com.spring.runmateapi.review.dto.ReviewDTO;
import com.spring.runmateapi.review.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDTO(Review review);
}
