package com.lguplus.assignment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String username;
    private String password;
    private String nickname;

    public MemberResponse(Long id, String username,String nickname) {
        this.id = id;
        this.username = username;
        this.nickname=nickname;
    }
}
