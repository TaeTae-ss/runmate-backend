package com.spring.runningLike;


import com.spring.runmateapi.runningLike.service.RunningLikeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RunningLikeServiceTests {

    @Autowired
    private RunningLikeService runningLikeService;

    //실제 존재하는 runningId, memberId로 바꿔서 테스트 해줘야함
    private final Long runningId = 7L;
    private final Long memberId = 7L;

    @Test
    public void testToggleLikeon() {
        //처음 누르면 좋아요 등록
        runningLikeService.toggleLike(runningId, memberId);

        boolean isLike = runningLikeService.isLiked(runningId, memberId);
        long count = runningLikeService.getLikeCount(runningId);

        log.info("좋아요 여부: {}", isLike);
        log.info("좋아요 수: {}", count);
    }

    @Test
    public void testToggleLikeOff() {
        // 이미 좋아요 상태에서 다시 누르면 취소
        runningLikeService.toggleLike(runningId, memberId); //킴
        runningLikeService.toggleLike(runningId, memberId); //끔

        boolean isLike = runningLikeService.isLiked(runningId, memberId);
        long count = runningLikeService.getLikeCount(runningId);

        log.info("좋아요 여부: {}", isLike); // flase여야 정상
        log.info("좋아요 수: {}", count); // 0이어야 정상
    }

    @Test
    public void testDuplicateLikePrevented() {
        // 같은 회원이 같은 모임에 두 번 좋아요를 눌러도(연속 호출) 중복 등록되면 안됨
        runningLikeService.toggleLike(runningId, memberId); //다시 좋아요 눌르고
        long countAfterFirst = runningLikeService.getLikeCount(runningId);

        //다른 회원이 같은 모임에 좋아요
        runningLikeService.toggleLike(runningId, 8L);
        long countAfterSecond = runningLikeService.getLikeCount(runningId);

        log.info("첫 좋아요 후 카운트: {}", countAfterFirst);
        log.info("두번째 회원 좋아요 후 카운트: {}", countAfterSecond);
    }


}
