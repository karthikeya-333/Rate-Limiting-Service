package com.service.Rate.Limiting.Service.rateService;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateLimitCheckResponse {
    private boolean allowed;
    private String reason;
}

