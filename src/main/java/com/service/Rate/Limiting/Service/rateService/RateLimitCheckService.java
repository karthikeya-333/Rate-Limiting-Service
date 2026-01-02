package com.service.Rate.Limiting.Service.rateService;

import com.service.Rate.Limiting.Service.projects.dto.RateLimitWindow;
import com.service.Rate.Limiting.Service.projects.model.RateLimit;
import com.service.Rate.Limiting.Service.projects.repository.ApiKeyRepository;
import com.service.Rate.Limiting.Service.projects.repository.RateLimitRepository;
import com.service.Rate.Limiting.Service.projects.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateLimitCheckService {

    private final RateLimitRepository rateLimitRepository;
    private final ApiKeyService apiKeyService;
    private final ApiKeyRepository apiKeyRepository;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public boolean check( String projectId,String apiKey, String dimension, String idempotencyKey) {

        String hashKey= apiKeyService.hash(apiKey);
        apiKeyRepository
                .findByProjectIdAndKeyHashAndIsActive(Long.valueOf(projectId), hashKey, true)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_API_KEY"));

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String idemKey = buildIdempotencyKey(projectId, idempotencyKey);
            if (stringRedisTemplate.hasKey(idemKey)) {
                return true;
            }
        }

        List<RateLimit> limits = rateLimitRepository.findByProjectId(Long.valueOf(projectId));

        for (RateLimit limit : limits) {
            if (!limit.getDimension().equals(dimension) && !limit.getDimension().equals("*")) {
                continue;
            }

            String redisKey = buildRedisKey(projectId, limit.getDimension(), limit.getRateLimitWindow().name());
            Integer count = redisTemplate.opsForValue().get(redisKey);
            if (count == null) count = 0;

            if (count >= limit.getLimitValue()) {
                return false;
            }

            redisTemplate.opsForValue().increment(redisKey);
            redisTemplate.expire(redisKey, getWindowDuration(limit.getRateLimitWindow()));
        }

        for (RateLimit limit : limits) {
            if (!limit.getDimension().equals(dimension) && !limit.getDimension().equals("*")) {
                continue;
            }

            String redisKey = buildRedisKey(projectId, limit.getDimension(), limit.getRateLimitWindow().name());
            redisTemplate.opsForValue().increment(redisKey);
            redisTemplate.expire(redisKey, getWindowDuration(limit.getRateLimitWindow()));
        }

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            String idemKey = buildIdempotencyKey(projectId, idempotencyKey);
            stringRedisTemplate.opsForValue().set(idemKey, "USED", getMaxWindowTTL(limits));
        }

        return true;
    }

    private String buildRedisKey(String projectId, String dimension, String window) {
        return "rl:" + projectId + ":" + dimension + ":" + window;
    }

    private Duration getWindowDuration(RateLimitWindow rateLimitWindow) {
        return switch (rateLimitWindow) {
            case MINUTE -> Duration.ofMinutes(1);
            case HOUR -> Duration.ofHours(1);
            default -> Duration.ofDays(1);
        };
    }

    private String buildIdempotencyKey(String projectId, String idempotencyKey) {
        return "idem:" + projectId + ":" + idempotencyKey;
    }
    
    private Duration getMaxWindowTTL(List<RateLimit> limits) {
        return limits.stream()
                .map(limit -> getWindowDuration(limit.getRateLimitWindow()))
                .max(Duration::compareTo)
                .orElse(Duration.ofHours(1));
    }
}
