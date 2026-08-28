package com.spring.runmateapi.running.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "running")
public class Running {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long runningId; //런닝모임ID

    @Column(nullable = false, length= 50)
    private String title; //제목

    @Column(nullable = false, length = 20)
    private String location; //지역

    @Column(nullable = false, length = 50)
    private String place; //장소

    @Column(name = "run_date", nullable = false)
    private LocalDate runDate; //러닝날짜

    @Column(nullable = false, length= 10)
    private String startTime; //시작시간

    @Column(nullable = false)
    private int runTime; //러닝시간

    @Column(nullable = false)
    private int distance; //거리

    @Column(nullable = false)
    private int maxPeople; //인원수

    @Column(nullable = true)
    private String content; //모임내용

    @Column(nullable = false)
    private boolean status; //모집상태

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt; //작성일

    @Column(nullable = false)
    private Long memberId; //대표회원번호

    @Builder
    public Running(String title, String location, String place, LocalDate runDate,
                   String startTime, int runTime, int distance, int maxPeople,
                   String content, boolean status, LocalDate createdAt, Long memberId) {
        this.title = title;
        this.location = location;
        this.place = place;
        this.runDate = runDate;
        this.startTime = startTime;
        this.runTime = runTime;
        this.distance = distance;
        this.maxPeople = maxPeople;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.memberId = memberId;
    }
    // 상태값 변경 CRUD
    public  void changeRunning(String title, String location, String place, LocalDate runDate, String startTime,
                               int runTime, int distance, int maxPeople, String content, boolean status) {
        this.title = title;
        this.location = location;
        this.place = place;
        this.runDate = runDate;
        this.startTime = startTime;
        this.runTime = runTime;
        this.distance = distance;
        this.maxPeople = maxPeople;
        this.content = content;
        this.status = status;
    }

    // status(true: 모집중, false: 모집마감)전용 상태값변경로직
    public void updateStatus(Boolean status) {
        this.status = status;
    }
}
