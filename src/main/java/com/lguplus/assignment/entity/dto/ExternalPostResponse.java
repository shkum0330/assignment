package com.lguplus.assignment.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalPostResponse {
    @JsonProperty("posts")
    private List<ExternalPostItem> posts;
}

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class ExternalPostItem {
    @JsonProperty("_source")
    private ExternalPostSource source;
}

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class ExternalPostSource {
    private String title; // 제목
    private String id;    // 링크
}