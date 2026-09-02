package com.spring.runmateapi.runningLike.cotroller;


import com.spring.runmateapi.runningLike.service.RunningLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class RunningLikeController {

    private final RunningLikeService runningLikeService;

    //좋아요 토글(누리면 등록, 다시 누르면 취소)
    @PostMapping("/runmate/runnings/{runningId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long runningId,
            @RequestBody Map<String, Long> request) {

        Long memberId = request.get("memberId");
        runningLikeService.toggleLike(runningId, memberId);

        long likeCount = runningLikeService.getLikeCount(runningId);
        boolean isLike = runningLikeService.isLiked(runningId, memberId);

        return ResponseEntity.ok(Map.of("likeCount", likeCount, "isLiked", isLike));
    }
    // 좋아요 여부 조회
    @GetMapping("/runmate/runnings/{runningId}/like")
    public ResponseEntity<Map<String, Object>> checkLiked(@PathVariable Long runningId, @RequestParam Long memberId) {
        boolean isLiked = runningLikeService.isLiked(runningId, memberId);
        long likeCount = runningLikeService.getLikeCount(runningId);

        return ResponseEntity.ok(Map.of("isLiked", isLiked, "likeCount", likeCount));
    }
}
