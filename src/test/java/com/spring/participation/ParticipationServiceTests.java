package com.spring.participation;


import com.spring.runmateapi.participation.service.ParticipationService;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.service.RunningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class ParticipationServiceTests {

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private RunningService runningService;

    //테스트용 모임 하나 만들어주기 (maxPeople = 2로 작게 설정해서 마감 테스트 쉽게)
    private Long createTestRunning() {
        RunningDTO runningDTO = new RunningDTO();
        runningDTO.setTitle("참가 테스트용 모임");
        runningDTO.setLocation("서울");
        runningDTO.setPlace("여의도");
        runningDTO.setRunDate(LocalDate.now().plusDays(6)); //미래 날짜로 (시작시간 마감 로직 안 걸리게)
        runningDTO.setStartTime("08:00");
        runningDTO.setDistance(5);
        runningDTO.setMaxPeople(2); // 정원 2명으로 작게
        runningDTO.setContent("참가 테스트");
        runningDTO.setStatus(true);
        runningDTO.setMemberId(1L);
        return runningService.register(runningDTO);
    }


}


