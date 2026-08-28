package com.spring.runmateapi.running.controller;


import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.service.RunningService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runmate/runnings")
public class RunningContoller {
    private final RunningService runningService;


    //등록
    @PostMapping
    @Operation(hidden = true)
    public ResponseEntity<Map<String, Long>> register(@RequestBody RunningDTO runningDTO) {
        Long runningId = runningService.register(runningDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("runningId", runningId));
    }
    //목록 조회(페이징)
    @GetMapping
    @Operation(hidden = true)
    public ResponseEntity<PageResponseDTO<RunningDTO>> list(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<RunningDTO> responseDTO = runningService.list(pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    //상세 조회
    @GetMapping("/{runningId}")
    @Operation(hidden = true)
    public ResponseEntity<RunningDTO> get(@PathVariable Long runningId) {
        RunningDTO runningDTO = runningService.get(runningId);
        return ResponseEntity.ok(runningDTO);
    }
    // 수정
    @PutMapping("/{runningId}")
    @Operation(hidden = true)
    public ResponseEntity<Map<String, String>> modify(@PathVariable(name = "runningId")
                                                      Long runningId, @RequestBody RunningDTO runningDTO) {
        runningDTO.setRunningId(runningId);
        runningService.modify(runningDTO);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
    // 삭제
    @DeleteMapping("/{runningId}")
    @Operation(hidden = true)
    public ResponseEntity<Map<String, String>> remove(@PathVariable(name = "runningId")Long runningId) {
        runningService.remove(runningId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
    // 상태 변경 (모집중 <-> 모집마감)
    @PatchMapping("/{runningId}/status")
    @Operation(hidden = true)
    public ResponseEntity<Map<String, String>> updateStatus(@PathVariable Long runningId,
                                                             @RequestBody Map<String, Boolean> request) {
        Boolean status = request.get("status");
        runningService.updateStatus(runningId, status);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

}
