package com.spring.runmateapi.running.repository;

import com.spring.runmateapi.running.entity.Running;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningRepository extends JpaRepository<Running, Long>, RunningRepositoryCustom{

}
