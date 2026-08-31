package com.spring.running;


import com.spring.runmateapi.common.dto.PageRequestDTO;
import com.spring.runmateapi.common.dto.PageResponseDTO;
import com.spring.runmateapi.running.dto.RunningDTO;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import com.spring.runmateapi.running.service.RunningService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class RunningServiceTests {
//    @Autowired
//    private RunningService runningService;
//    private PageRequestDTO pageRequestDTO;
//    private PageResponseDTO pageResponseDTO;
//    @Autowired
//    private RunningRepository runningRepository;
//
//    @Test
//    public void TestRegister() {
//        RunningDTO runningDTO = new RunningDTO();
//        runningDTO.setTitle("탄천 러닝 모임");
//        runningDTO.setLocation("성남");
//        runningDTO.setPlace("탄천 야외무대");
//        runningDTO.setRunDate(LocalDate.of(2026, 9, 10));
//        runningDTO.setStartTime("07:00");
//        runningDTO.setRunTime(50);
//        runningDTO.setDistance(6);
//        runningDTO.setMaxPeople(12);
//        runningDTO.setContent("아침 러닝 같이 하실 분 구합니다!");
//        runningDTO.setStatus(true);
//        runningDTO.setMemberId(1L);
//
//        Long runningId = runningService.register(runningDTO);
//        log.info("등록된 번호: {}", runningId);
//    }
//    //검색 테스트
//    @Test
//    public void testSearchList() {
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//        pageRequestDTO.setLocation("서울");
//        pageRequestDTO.setSort("latest");
//
//        PageResponseDTO<RunningDTO> result = runningService.list(pageRequestDTO);
//        log.info("검색 결과 수: {}", result.getTotalCount());
//        result.getDtoList().forEach(dto -> log.info("제목: {}", dto.getTitle()));
//    }
//
//    //status값 변경 테스트
//    @Test
//    public void testTimeExpiredStatus() {
//        //과거 날짜의 러닝 데이터를 하나 등록해서
//        Running expired = Running.builder()
//                .title("status테스트 어제 지난 모임")
//                .location("서울")
//                .place("여의도한강공원")
//                .runDate(LocalDate.now().minusDays(1))
//                .startTime("09:00")
//                .runTime(60)
//                .distance(10)
//                .content("테스트용")
//                .status(true) // DB엔 모집중으로 저장
//                .memberId
//                .build();
//        Running saved = runningRepository.save(expired);
//
//        RunningDTO runningDTO = runningService.get(saved.getRunningId());
//        log.info("실제 반환된 status: {}", runningDTO.isStatus()); // false가 나와야 정상임.
//    }
}
