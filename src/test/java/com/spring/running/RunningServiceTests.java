package com.spring.running;


import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.service.RunningService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class RunningServiceTests {
    @Autowired
    private RunningService runningService;

    @Test
    public void TestRegister() {
        RunningDTO runningDTO = new RunningDTO();
        runningDTO.setTitle("탄천 러닝 모임");
        runningDTO.setLocation("성남");
        runningDTO.setPlace("탄천 야외무대");
        runningDTO.setRunDate(LocalDate.of(2026, 9, 10));
        runningDTO.setStartTime("07:00");
        runningDTO.setRunTime(50);
        runningDTO.setDistance(6);
        runningDTO.setMaxPeople(12);
        runningDTO.setContent("아침 러닝 같이 하실 분 구합니다!");
        runningDTO.setStatus(true);
        runningDTO.setMemberId(3L);

        Long runningId = runningService.register(runningDTO);
        log.info("등록된 번호: {}", runningId);
    }
}
