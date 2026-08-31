package com.spring.runmateapi.member.service;

import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
public class MemberServiceTests {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Commit
    public void testInsertMember() {
        Member member = new Member(
                "test02",
                "1234",
                "테스트회원2",
                "test02@test.com",
                "010-2345-6789"
        );

        memberRepository.save(member);
    }

}
