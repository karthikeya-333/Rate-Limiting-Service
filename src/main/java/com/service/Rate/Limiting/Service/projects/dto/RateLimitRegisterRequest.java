package com.service.Rate.Limiting.Service.projects.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RateLimitRegisterRequest {

    @NotNull
    private UUID projectId;

    @NotNull
    private String dimension;

    @NotNull
    private Window window;

    @NotNull
    private Integer limitValue;

}


