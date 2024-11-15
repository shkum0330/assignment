package com.lguplus.assignment.entity.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
}
