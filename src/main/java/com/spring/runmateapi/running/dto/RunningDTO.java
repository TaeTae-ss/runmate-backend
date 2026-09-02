package com.spring.runmateapi.running.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunningDTO {
    private Long runningId;
    private String title;
    private String location;
    private String place;
    private Double latitude; //위도
    private Double longitude; //경도
    private String startTime;
    private int runTime;
    private int distance;
    private int maxPeople;
    private String content;
    private boolean status;
    private Long likeCount; // 좋아요 수
    private Long memberId;
    private String memberNickname;

    //json변환시 문자열로 표시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate runDate;
    private LocalDate createdAt;
}
