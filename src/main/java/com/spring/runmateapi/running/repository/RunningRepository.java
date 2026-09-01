package com.spring.runmateapi.running.repository;

import com.spring.runmateapi.running.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningRepository extends JpaRepository<Running, Long>, RunningRepositoryCustom{
    List<Running> findByMember_MemberId(Long memberId);

}
