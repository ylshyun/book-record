package com.boot.controller;

import com.boot.config.security.CustomMemberDetails;
import com.boot.dto.MemberInfoDTO;
import com.boot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberRestController {

    private final MemberService memberService;

    // 아메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = memberService.emailExists(email);
        Map<String, Boolean> response = Map.of("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 회원정보 수정시 비밀번호 인증
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody MemberInfoDTO request,
                                            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        // 비밀번호 null
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "비밀번호를 입력하세요."));
        }

        boolean isValid = memberService.validatePassword(memberDetails.getUsername(), request.getPassword());
        if (isValid) {
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "비밀번호가 일치하지 않습니다."));
        }
    }
}
