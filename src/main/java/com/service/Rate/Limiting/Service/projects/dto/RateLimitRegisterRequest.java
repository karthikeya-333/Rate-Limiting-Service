package com.service.Rate.Limiting.Service.projects.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RateLimitRegisterRequest {

    @NotNull
    private Long projectId;

    @NotNull
    private String dimension;

    @NotNull
    private RateLimitWindow rateLimitWindow;

    @NotNull
    private Integer limitValue;

}


