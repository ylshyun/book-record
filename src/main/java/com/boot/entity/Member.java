package com.boot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    @Column(name = "member_password")
    private String memberPassword;

    @Id
    @Column(name = "member_email", unique = true)
    private String memberEmail;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_role")
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Member(String memberPassword, String memberEmail, String memberName, String role) {
        this.memberPassword = memberPassword;
        this.memberEmail = memberEmail;
        this.memberName = memberName;
        this.role = role;
    }

    public void update(String memberPassword, String memberName) {
        this.memberPassword = memberPassword;
        this.memberName = memberName;
    }
}

