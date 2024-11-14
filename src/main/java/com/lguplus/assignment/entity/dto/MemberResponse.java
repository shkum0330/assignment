package com.lguplus.assignment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
    private Long memberId;
    private String username;
    private String password;
    private String nickname;

    public MemberResponse(Long memberId, String username, String nickname) {
        this.memberId = memberId;
        this.username = username;
        this.nickname=nickname;
    }
}
