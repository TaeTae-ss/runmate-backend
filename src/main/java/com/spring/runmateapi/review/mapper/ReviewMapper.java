package com.spring.runmateapi.review.mapper;

import com.spring.runmateapi.review.dto.ReviewDTO;
import com.spring.runmateapi.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "running.runningId", target = "runningId")
    @Mapping(source = "member.nickname", target = "memberNickname")
    ReviewDTO toDTO(Review review);
}
