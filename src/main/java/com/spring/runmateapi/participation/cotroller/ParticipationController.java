package com.spring.runmateapi.participation.cotroller;


import com.spring.runmateapi.participation.dto.ParticipationDTO;
import com.spring.runmateapi.participation.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runmate")
public class ParticipationController {

    private final ParticipationService participationService;

    //참가 신청
    @PostMapping("/runnings/{runningId}/participation")
    public ResponseEntity<Map<String, Long>> apply(@PathVariable Long runningId,
                                                   @RequestBody ParticipationDTO participationDTO) {
        participationDTO.setRunningId(runningId);
        Long participationId = participationService.apply(participationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("participationId", participationId));
    }

    // 참가 취소
    @DeleteMapping("/runnings/{runningId}/participation/{memberId}")
    public ResponseEntity<Map<String, String>> cancel(@PathVariable Long runningId,
                                                      @PathVariable Long memberId) {
        participationService.cancel(runningId, memberId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    //모임별 참가자 목록 조회(모임작용)
    @GetMapping("/runnings/{runningId}/participation")
    public ResponseEntity<List<ParticipationDTO>> listByRunning(@PathVariable Long runningId) {
        return ResponseEntity.ok(participationService.getListByRunning(runningId));
    }

    //회원별 참가 목록 조회
    @GetMapping("/member/{memberId}/participation")
    public ResponseEntity<List<ParticipationDTO>> listByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(participationService.getListByMember(memberId));
    }
}
