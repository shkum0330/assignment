package com.lguplus.assignment.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRequest {
    private String username;
    private String password;
    private String nickname;
}
