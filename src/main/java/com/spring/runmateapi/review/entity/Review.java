package com.spring.runmateapi.review.entity;

import com.spring.runmateapi.member.entity.Member;
import com.spring.runmateapi.running.entity.Running;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "running_id", nullable = false)
    private Running running;

    public Review(int rating, String content, Member member, Running running) {
        this.rating = rating;
        this.content = content;
        this.member = member;
        this.running = running;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    public void changeReview(String content) {
        this.content = content;
    }
}
