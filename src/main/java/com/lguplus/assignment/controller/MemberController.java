package com.lguplus.assignment.controller;

import com.lguplus.assignment.entity.dto.MemberRequest;
import com.lguplus.assignment.entity.dto.MemberResponse;
import com.lguplus.assignment.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRequest memberRequest) {
        memberService.register(memberRequest);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequest memberRequest) {
      String token = memberService.login(memberRequest);
      return ResponseEntity.ok(token);
    }

    @GetMapping("/members")
    public ResponseEntity<MemberResponse> getMemberDetails(@RequestHeader("Authorization") String token) {
        MemberResponse memberResponse = memberService.getMemberDetails(token);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/members")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String token) {
        memberService.deleteMember(token);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
