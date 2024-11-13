package com.lguplus.assignment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String username;
    private String password;

    public MemberResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
