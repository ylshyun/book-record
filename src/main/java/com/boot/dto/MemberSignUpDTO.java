package com.boot.dto;

import com.boot.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpDTO {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, max = 15, message = "이름은 2자 이상 15자 이하로 입력해주세요.")
    private String memberName;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String memberEmail;

    private String memberEmailOption;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String memberPassword;

    @Builder
    public MemberSignUpDTO(String memberName, String memberEmail, String memberEmailOption, String memberPassword) {
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberEmailOption = memberEmailOption;
        this.memberPassword = memberPassword;
    }

    public Member toEntity(String encodedPassword) {
        String memberTotalEmail = memberEmail + "@" + memberEmailOption;
        return Member.builder()
                .memberName(memberName)
                .memberEmail(memberTotalEmail)
                .memberPassword(encodedPassword)
                .role("ROLE_MEMBER")
                .build();
    }
}