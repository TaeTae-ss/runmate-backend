package com.spring.runmateapi.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String phone;

    // 회원의 권한 정보를 별도의 테이블에 저장
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name="member_role",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "role")
    private Set<MemberRole> roles = new HashSet<>();

    // 회원 권한 추가
    public void addRole(MemberRole memberRole) {
        roles.add(memberRole);
    }
    // 회원 권한 삭제
    public void removeRole(MemberRole memberRole) {
        roles.remove(memberRole);
    }


    @Column(nullable = false)
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    public Member(String userId, String password, String nickname, String email, String phone) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;

        // 신규 회원은 기본적으로 USER 권한 부여
        this.roles.add(MemberRole.USER);
    }


    public void changePw(String password) { this.password = password;}

    public void  changeNickname(String nickname) { this.nickname = nickname; }

    public void changePhone(String phone) { this.phone = phone; }

}
