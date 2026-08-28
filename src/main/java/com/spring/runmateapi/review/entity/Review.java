package com.spring.runmateapi.review.entity;

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

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long runningId;

    public Review(int rating, String content, Long memberId, Long runningId) {
        this.rating = rating;
        this.content = content;
        this.memberId = memberId;
        this.runningId = runningId;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    public void changeReview(String content) {
        this.content = content;
    }
}
