package com.spring.runmateapi.participation.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "participation")
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    @Column(name = "running_id", nullable = false)
    private  Long runningId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "joined_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate joinedAt;

    @Builder
    public Participation(Long runningId, Long memberId){
        this.runningId = runningId;
        this.memberId = memberId;
    }
}
