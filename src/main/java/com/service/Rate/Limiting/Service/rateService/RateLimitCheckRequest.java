package com.service.Rate.Limiting.Service.rateService;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RateLimitCheckRequest {
    @NotBlank(message = "Dimension cannot be blank")
    private String dimension;

    @NotBlank(message = "Project ID cannot be blank")
    private String projectId;

}
