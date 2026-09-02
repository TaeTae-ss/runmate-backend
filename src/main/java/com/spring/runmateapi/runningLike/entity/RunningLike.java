package com.spring.runmateapi.runningLike.entity;


import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.running.entity.Running;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "running_like",
                uniqueConstraints = @UniqueConstraint(columnNames = {"running_id", "member_id"}))
public class RunningLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_id", nullable = false)
    private Running running;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Builder
    public RunningLike(Running running, Member member) {
        this.running = running;
        this.member = member;
    }
}
