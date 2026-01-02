package com.service.Rate.Limiting.Service.rateService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RateLimitCheckController {

    private final RateLimitCheckService rateLimitCheckService;

    @PostMapping("/check")
    public ResponseEntity<RateLimitCheckResponse> check(
            @RequestHeader("X-API-Key") @Valid String apiKey,
            @RequestBody @Valid RateLimitCheckRequest request
    ) {

        boolean allowed = rateLimitCheckService.check(
                request.getProjectId(),
                apiKey,
                request.getDimension()
        );

        if (!allowed) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new RateLimitCheckResponse(
                            false,
                            "RATE_LIMIT_EXCEEDED"
                    ));
        }

        return ResponseEntity.ok(
                new RateLimitCheckResponse(
                        true,
                        "ALLOWED"
                )
        );
    }
}
