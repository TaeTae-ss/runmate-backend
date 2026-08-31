package com.spring.runmateapi.review;

import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.review.entity.Review;
import com.spring.runmateapi.review.repository.ReviewRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RunningRepository runningRepository;

    private void printReview(Review review) {
        log.info("후기번호: {}", review.getReviewId());
        log.info("평점: {}", review.getRating());
        log.info("내용: {}", review.getContent());
        log.info("작성일: {}", review.getCreatedAt());
    }

    @Test
    @Commit
    public void testInsertReview() {

        // 기존에 DB에 있는 회원 조회
        Member member = memberRepository.findById(2L)
                .orElseThrow(() ->
                        new IllegalArgumentException("1번 회원이 존재하지 않습니다."));

        // 기존에 DB에 있는 러닝모임 조회
        Running running = runningRepository.findById(1L)
                .orElseThrow(() ->
                        new IllegalArgumentException("1번 러닝모임이 존재하지 않습니다."));


        Review review = new Review(
                4,                              // rating
                "오늘 런닝 정말 즐거웠습니다. 감사합니다.", // content
                member,                             // memberId (실제 존재하는 회원번호로 맞춰주세요)
                running                              // runningId (실제 존재하는 모임번호로 맞춰주세요)
        );

        Review savedReview = reviewRepository.save(review);
        printReview(savedReview);
    }
}
