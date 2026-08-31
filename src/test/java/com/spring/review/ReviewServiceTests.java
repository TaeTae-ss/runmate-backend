package com.spring.runmateapi.review;

import com.spring.runmateapi.review.entity.Review;
import com.spring.runmateapi.review.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class ReviewServiceTests {

    @Autowired
    private ReviewRepository reviewRepository;

    private void printReview(Review review) {
        log.info("후기번호: {}", review.getReviewId());
        log.info("평점: {}", review.getRating());
        log.info("내용: {}", review.getContent());
        log.info("작성일: {}", review.getCreatedAt());
        log.info("작성회원번호: {}", review.getMemberId());
        log.info("러닝모임번호: {}", review.getRunningId());
    }

    @Test
    @Commit
    public void testInsertReview() {
        Review review = new Review(
                4,                              // rating
                "오늘 런닝 정말 즐거웠습니다. 감사합니다.", // content
                2L,                             // memberId (실제 존재하는 회원번호로 맞춰주세요)
                2L                              // runningId (실제 존재하는 모임번호로 맞춰주세요)
        );

        Review savedReview = reviewRepository.save(review);
        printReview(savedReview);
    }
}
