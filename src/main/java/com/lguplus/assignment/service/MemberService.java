package com.lguplus.assignment.service;

import com.lguplus.assignment.entity.Member;
import com.lguplus.assignment.entity.dto.MemberRequest;
import com.lguplus.assignment.entity.dto.MemberResponse;
import com.lguplus.assignment.global.exception.DuplicateUsernameException;
import com.lguplus.assignment.global.jwt.JwtUtil;
import com.lguplus.assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.lguplus.assignment.global.util.PasswordUtil.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public void register(MemberRequest memberRequest) {
        if (memberRepository.existsByUsername(memberRequest.getUsername())) {
            throw new DuplicateUsernameException("중복된 아이디를 사용할 수 없습니다.");
        }
        if (memberRepository.existsByNickname(memberRequest.getUsername())) {
            throw new DuplicateUsernameException("중복된 아이디를 사용할 수 없습니다.");
        }
        Member member = Member.builder()
                .username(memberRequest.getUsername())
                .password(encode(memberRequest.getPassword()))
                .nickname(memberRequest.getNickname())
                .build();

        memberRepository.save(member);
    }

    public String login(MemberRequest memberRequest) {
        Member member = memberRepository.findByUsername(memberRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!matches(memberRequest.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return jwtUtil.generateToken(member.getMemberId());
    }

    public MemberResponse getMemberDetails(String token) {
        Long memberId = jwtUtil.validateToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new MemberResponse(memberId, member.getUsername(), member.getNickname());
    }

    public void deleteMember(String token) {
        Long memberId = jwtUtil.validateToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        memberRepository.delete(member);
    }
}
