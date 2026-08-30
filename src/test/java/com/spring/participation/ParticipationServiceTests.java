package com.spring.participation;


import com.spring.runmateapi.participation.dto.ParticipationDTO;
import com.spring.runmateapi.participation.service.ParticipationService;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.service.RunningService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;

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
        runningDTO.setRunTime(60);
        runningDTO.setDistance(5);
        runningDTO.setMaxPeople(2); // 정원 2명으로 작게
        runningDTO.setContent("참가 테스트");
        runningDTO.setStatus(true);
        runningDTO.setMemberId(1L);
        return runningService.register(runningDTO);
    }
    // 신청이 잘되는지 신청 확인테스트
    @Test
    @Commit
    public void testApply() {
        Long runningId = createTestRunning();

        ParticipationDTO dto = new ParticipationDTO();
        dto.setRunningId(runningId);
        dto.setMemberId(10L);

        Long participationId = participationService.apply(dto);
        log.info("참가 신청 완료, participation: {}", participationId);
    }
    // 중복신청 확인 테스트
    @Test
    @Commit
    public void testDuplicateApply() {
        Long runningId = createTestRunning();

        ParticipationDTO dto = new ParticipationDTO();
        dto.setRunningId(runningId);
        dto.setMemberId(10L);

        participationService.apply(dto); //1차 신청 성공

        try{
            participationService.apply(dto); // 2차 신청 (중복)
            log.error("중복 신청이 막히지 않음! 버그있음");
        } catch (IllegalStateException e) {
            log.info("정상적으로 중복 신청 차단됨: {}", e.getMessage());
        }
    }
    // 인원 수 찼을때 status변화 테스트 코드 덤으로 삭제까지볼수있다.
    @Test
    @Commit
    public void testCancelReopensRunning() {
        Long runningId = createTestRunning(); // maxPeople = 2

        ParticipationDTO dto1 = new ParticipationDTO();
        dto1.setRunningId(runningId);
        dto1.setMemberId(21L);
        participationService.apply(dto1);

        ParticipationDTO dto2 = new ParticipationDTO();
        dto2.setRunningId(runningId);
        dto2.setMemberId(22L);
        participationService.apply(dto2);

        RunningDTO afterFull = runningService.get(runningId);
        log.info("정원 다 찬 후 status: {}", afterFull.isStatus()); // false(모집 마감)이 나와야 정상

        //1명 취소
        participationService.cancel(runningId, 21L);

        RunningDTO afterCancel = runningService.get(runningId);
        log.info("취소 후 status: {}", afterCancel.isStatus()); // true(모집 중)이여야 정상
    }
    // 참가자쪽 조회 테스트
    @Test
    public void testListByRunning() {
        Long runningId = createTestRunning();

        ParticipationDTO dto = new ParticipationDTO();
        dto.setRunningId(runningId);
        dto.setMemberId(31L);
        participationService.apply(dto);

        List<ParticipationDTO> list = participationService.getListByRunning(runningId);
        list.forEach(p -> log.info("참가자 memberId: {}", p.getMemberId()));
    }
    // 참가한 런닝모임 조회 테스트
    @Test
    public void testListByMember() {
        Long runningId = createTestRunning();

        ParticipationDTO dto = new ParticipationDTO();
        dto.setRunningId(runningId);
        dto.setMemberId(41L);
        participationService.apply(dto);

        List<ParticipationDTO> list = participationService.getListByMember(41L);
        list.forEach(p ->log.info("참가한 runningId: {}", p.getRunningId()));
    }

}


