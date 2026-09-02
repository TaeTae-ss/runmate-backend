package com.spring.runmateapi.runningLike.service;

public interface RunningLikeService {
    void toggleLike(Long runningId, Long memberId); // 좋아요 눌렀다 취소 눌렀다 토글
    long getLikeCount(Long runningId);
    boolean isLiked(Long runningId, Long memberId);
}
