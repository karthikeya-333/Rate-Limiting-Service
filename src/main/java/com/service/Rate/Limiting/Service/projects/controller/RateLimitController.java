package com.service.Rate.Limiting.Service.projects.controller;


import com.service.Rate.Limiting.Service.auth.service.JwtService;
import com.service.Rate.Limiting.Service.common.ApiResponse;
import com.service.Rate.Limiting.Service.projects.dto.RateLimitRegisterRequest;
import com.service.Rate.Limiting.Service.projects.dto.RateLimitResponse;
import com.service.Rate.Limiting.Service.projects.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/ratelimits")
@RequiredArgsConstructor
public class RateLimitController {

    private final RateLimitService rateLimitAdminService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<RateLimitResponse>> createOrUpdate(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody RateLimitRegisterRequest req) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        RateLimitResponse limit = rateLimitAdminService.createOrUpdateLimit(req,userId);
        return ResponseEntity.ok(
                ApiResponse.<RateLimitResponse>builder()
                        .success(true)
                        .message("Rate limit created/updated successfully")
                        .data(limit)
                        .build()
        );
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<RateLimitResponse>>> getProjectLimits(@RequestHeader("Authorization") String authHeader, @PathVariable Long projectId) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        List<RateLimitResponse> limits = rateLimitAdminService.getLimitsForProject(projectId,userId);
        return ResponseEntity.ok(
                ApiResponse.<List<RateLimitResponse>>builder()
                        .success(true)
                        .message("Fetched rate limits for project")
                        .data(limits)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLimit(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        rateLimitAdminService.deleteLimit(id,userId);
        return ResponseEntity.noContent().build();
    }
}

