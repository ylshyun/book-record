package com.boot.controller;

import com.boot.config.security.CustomMemberDetails;
import com.boot.dto.*;
import com.boot.entity.Member;
import com.boot.repository.MemberRepository;
import com.boot.service.MemberService;
import com.boot.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signup(@ModelAttribute("memberSignUpDTO") MemberSignUpDTO memberSignUpDTO) {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signupForm(@Valid MemberSignUpDTO memberSignUpDTO, BindingResult bindingResult, Model model) {
        // 유효성 검사 실패 시
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupError", bindingResult.getAllErrors());
            return "member/signup";
        }

        try {
            // 비밀번호 유효성 검사
            memberService.validatePasswordAndEmail(memberSignUpDTO.getMemberPassword(), memberSignUpDTO.getMemberEmail());

            // 회원 생성
            memberService.create(memberSignUpDTO);
            return "redirect:login";

        } catch (IllegalArgumentException e) {
            // 예외 발생 시 메시지를 model에 담아 Thymeleaf로 전달
            model.addAttribute("customError", e.getMessage());
            return "member/signup";
        }
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomMemberDetails memberDetails, Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        Page<ReviewBookDTO> reviews = reviewService.getReviewsWithBooks(memberDetails.getUsername(), page, size);
        model.addAttribute("memberName", memberDetails.getName());
        model.addAttribute("memberEmail", memberDetails.getUsername());
        model.addAttribute("currentPage", reviews.getNumber());
        model.addAttribute("totalPages", reviews.getTotalPages());
        model.addAttribute("reviews", reviews);
        return "member/mypage";
    }

    // 회원정보 수정 페이지
    @GetMapping("/member/update")
    public String updatePage(@AuthenticationPrincipal CustomMemberDetails memberDetails, Model model) {
        String memberEmail = memberDetails.getUsername();
        String memberName = memberDetails.getName();

        MemberSignUpDTO memberDTO = MemberSignUpDTO.builder()
                .memberName(memberName)
                .memberEmail(memberEmail)
                .build();

        model.addAttribute("memberDTO", memberDTO);

        return "member/update";
    }

    // 회원정보 수정
    @PutMapping("/member/update-info")
    public String updateMemberInfo(@Valid @ModelAttribute("memberDTO") MemberSignUpDTO memberDTO,
                                   BindingResult bindingResult, Model model,
                                   @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("updateError", bindingResult.getAllErrors());
            return "member/update";
        }

        try {
            // 비밀번호 & 이메일 검증
            memberService.validatePasswordAndEmail(memberDTO.getMemberPassword(), memberDTO.getMemberEmail());

            // 회원 정보 수정
            memberService.updateMemberInfo(memberDTO, memberDetails.getUsername());

            // 수정된 회원 정보를 다시 가져오기
            Member updatedMember = memberRepository.findByMemberEmail(memberDetails.getUsername());

            // 새로운 SecurityContext 업데이트
            CustomMemberDetails updatedMemberDetails = CustomMemberDetails.builder()
                    .name(updatedMember.getMemberName())
                    .password(updatedMember.getMemberPassword())
                    .username(updatedMember.getMemberEmail())
                    .build();

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedMemberDetails,
                    updatedMemberDetails.getPassword(),
                    updatedMemberDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return "redirect:/mypage";

        } catch (IllegalArgumentException e) {
            model.addAttribute("customError", e.getMessage());
            return "member/update";
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/member/delete")
    public String deleteMember(@AuthenticationPrincipal CustomMemberDetails memberDetails,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        Member member = memberRepository.findByMemberEmail(memberDetails.getUsername());
        if (member == null) {
            throw new UsernameNotFoundException(memberDetails.getUsername());
        }
        memberService.delete(member);

        // 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, null);

        // 탈퇴 완료 메시지 리다이렉트 메인 페이지로 설정
        redirectAttributes.addFlashAttribute("message", "회원탈퇴가 완료되었습니다.");
        return "redirect:/";
    }
}

