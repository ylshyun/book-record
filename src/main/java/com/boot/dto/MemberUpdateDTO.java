package com.boot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberUpdateDTO {

    @NotBlank(message = "이름은 필수로 입력해야 합니다.")
    @Size(min = 2, max = 15, message = "이름은 2자 이상 15자 이하로 입력해주세요.")
    private String memberName;

    private String memberPassword;
    private String memberEmail;
}
