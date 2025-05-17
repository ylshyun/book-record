package com.boot.dto;

import com.boot.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDTO {
    private String memberEmail;
    private String memberName;

    @Builder
    public MemberDTO(String memberEmail, String memberName) {
        this.memberEmail = memberEmail;
        this.memberName = memberName;
    }

    public Member toEntity() {
        return Member.builder()
                .memberEmail(memberEmail)
                .memberName(memberName)
                .build();
    }

    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .memberEmail(member.getMemberEmail())
                .memberName(member.getMemberName())
                .build();
    }
}