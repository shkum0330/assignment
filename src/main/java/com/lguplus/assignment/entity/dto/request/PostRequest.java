package com.lguplus.assignment.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
