package com.boot;

import com.boot.dto.MemberSignUpDTO;
import com.boot.entity.Member;
import com.boot.repository.MemberRepository;
import com.boot.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @Test
    void createMember() {    // 회원가입
        // given
        String Password = "testerPassword";

        MemberSignUpDTO memberSignUpDTO = MemberSignUpDTO.builder()
                .memberEmail("test")
                .memberEmailOption("gmail.com")
                .memberPassword(Password)
                .memberName("테스터")
                .build();

        // PasswordEncoder는 매번 다른 값이 해싱되기 때문에 임의로 암호화 된 비밀번호 지정
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(Password)).thenReturn(encodedPassword);

        // when
        memberService.create(memberSignUpDTO);

        // then
        // 회원 정보가 정확하게 입력되었는지 확인하기 위해 ArgumentCaptor 사용
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(memberCaptor.capture());

        Member member = memberCaptor.getValue();
        String expectedEmail = memberSignUpDTO.getMemberEmail() + "@" + memberSignUpDTO.getMemberEmailOption();
        assertThat(member.getMemberEmail()).isEqualTo(expectedEmail);
        assertThat(member.getMemberPassword()).isEqualTo(encodedPassword);
        assertThat(member.getMemberName()).isEqualTo(memberSignUpDTO.getMemberName());
    }
}
