package com.lguplus.assignment.controller;

import com.lguplus.assignment.domain.dto.MemberRequest;
import com.lguplus.assignment.domain.dto.MemberResponse;
import com.lguplus.assignment.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRequest memberRequest) {
        try {
            memberService.register(memberRequest);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequest memberRequest) {
        try {
            String token = memberService.login(memberRequest);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberDetails(@RequestHeader("Authorization") String token) {
        MemberResponse memberResponseDTO = memberService.getMemberDetails(token);
        return ResponseEntity.ok(memberResponseDTO);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String token) {
        memberService.deleteMember(token);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
