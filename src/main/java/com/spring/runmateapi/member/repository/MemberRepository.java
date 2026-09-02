package com.spring.runmateapi.member.repository;

import com.spring.runmateapi.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원을 조회할 때 지연 로딩으로 설정된 권한 목록(memberRoleList)도 함께 조회한다.
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByUserId(String userId);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);

    // 이메일 중복 확인
    boolean existsByEmail(String email);
}
