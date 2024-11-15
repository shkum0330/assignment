package com.lguplus.assignment.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
