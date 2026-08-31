package com.spring.running;

import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import com.spring.runmateapi.running.entity.Running;
import com.spring.runmateapi.running.repository.RunningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class RuningRepositoryTests {
    @Autowired
    private RunningRepository runningRepository;
    @Autowired
    private MemberRepository memberRepository;

    private void printRunning(Running running) {
        log.info("제목: {}", running.getTitle());
        log.info("지역: {}", running.getLocation());
        log.info("장소: {}", running.getPlace());
        log.info("러닝 날짜: {}", running.getRunDate());
        log.info("거리: {}", running.getDistance());
        log.info("모집인원:{}", running.getMaxPeople());
        log.info("모임내용: {}", running.getContent());
        log.info("모집상태: {}", running.isStatus());
        log.info("등록일: {}", running.getCreatedAt());
    }

    @Test
    @Commit
    public void testInsertRunning() {
        Member member1 = memberRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을수 없습니다."));

        Member member2 = memberRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        Running running = Running.builder()
                .title("한강호수공원 급구")
                .location("서울")
                .place("뚝섬유원지")
                .runDate(LocalDate.of(2026, 9, 05))
                .startTime("09:00")
                .runTime(60)
                .distance(5)
                .maxPeople(10)
                .content("부담없이 같이 뛸사람 구합니다~")
                .status(true)
                .member(member1)
                .build();
        Running savedRunning = runningRepository.save(running);

        printRunning(savedRunning);

        Running running2 = Running.builder()
                .title("광교호수공원")
                .location("수원")
                .place("원천호수광장")
                .runDate(LocalDate.of(2026, 9, 05))
                .startTime("19:30")
                .runTime(45)
                .distance(7)
                .maxPeople(8)
                .content("퇴근 후 가볍게 야간 러닝 하실분 구합니다.")
                .status(true)
                .member(member2)
                .build();
        Running savedRunning2 = runningRepository.save(running2);

        printRunning(savedRunning2);

    }
    @Test
    public void testRead() {
        Long runningId = 1L;
        Running running = runningRepository.findById(runningId)
                .orElseThrow(() ->
                        new IllegalArgumentException(runningId + "번 모임기록이 없습니다."));
        log.info("데이터 조화: {}", runningId);
        printRunning(running);
    }

    @Test
    @Commit
    public void testModify() {
        Long runningId = 2L;
        Running running = runningRepository.findById(runningId)
                .orElseThrow(() ->
                        new IllegalArgumentException(runningId + "번 모임기록이 없습니다."));
        running.changeRunning(
                "한강 러닝 크루 모집 (수정)",
                "서울",
                "반포 한강공원",
                LocalDate.of(2026, 9, 20),
                "19:00",
                90,
                7,
                15,
                "장소가 변경되었습니다. 참고 부탁드려요!",
                true
        );
        printRunning(running);
    }

    @Test
    @Commit
    public  void testDelete() {
        Long runningId = 3L;
        if (runningRepository.existsById(runningId)) {
            runningRepository.deleteById(runningId);
            log.info("{}번 모임을 삭제했습니다.");
        } else {
            log.info("{}번 모임이 존재하지 않습니다.", runningId);
        }
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "runningId"
        );
        Page<Running> runningPage = runningRepository.findAll(pageable);
        log.info("전체 데이터 수: {}", runningPage.getTotalElements());

        List<Running> runningList = runningPage.getContent();
        runningList.forEach(this::printRunning);
    }
}
