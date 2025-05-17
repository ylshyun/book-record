package com.boot.service;

import com.boot.dto.MemberSignUpDTO;
import com.boot.entity.Member;
import com.boot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 존재 유무 확인
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return memberRepository.existsMemberByMemberEmail(email);
    }

    //회원가입
    @Transactional
    public void create(MemberSignUpDTO memberSignUpDTO) {

        // 비밀번호, 이메일 유효성 검사
        validatePasswordAndEmail(memberSignUpDTO.getMemberPassword(), memberSignUpDTO.getMemberEmail());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberSignUpDTO.getMemberPassword());

        // DTO -> Entity 변환
        Member member = memberSignUpDTO.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    //  비밀번호 유효성 검사
    @Transactional(readOnly = true)
    public void validatePasswordAndEmail(String memberPassword, String memberEmail) {
        if (memberPassword.contains(memberEmail)){
            throw new IllegalArgumentException("비밀번호에는 이메일을 포함할 수 없습니다.");
        }
    }

    // 비밀번호 인증
    @Transactional(readOnly = true)
    public boolean validatePassword(String email, String currentPassword) {
        // 이메일로 회원 정보 조회
        Member member = memberRepository.findByMemberEmail(email);
        // 비밀번호 비교
        return passwordEncoder.matches(currentPassword, member.getMemberPassword());
    }

    // 회원정보 수정
    @Transactional
    public void updateMemberInfo(MemberSignUpDTO memberDTO, String username) {
        Member member = memberRepository.findByMemberEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        // 비밀번호가 변경된 경우
        if (memberDTO.getMemberPassword() != null && !memberDTO.getMemberPassword().isBlank()) {
            // 비밀번호 유효성 검사
            validatePasswordAndEmail(memberDTO.getMemberPassword(), member.getMemberEmail());
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(memberDTO.getMemberPassword());
            member.update(encodedPassword, memberDTO.getMemberName());
        } else {
            // 비밀번호 변경이 없는 경우 기존 비밀번호를 유지하고 이름만 수정
            member.update(member.getMemberPassword(), memberDTO.getMemberName());
        }
    }

    // 회원 탈퇴
    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member);
    }
}
