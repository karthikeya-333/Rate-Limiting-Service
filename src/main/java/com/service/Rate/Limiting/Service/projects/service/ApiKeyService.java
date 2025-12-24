package com.service.Rate.Limiting.Service.projects.service;

import com.service.Rate.Limiting.Service.projects.model.ApiKey;
import com.service.Rate.Limiting.Service.projects.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Value("${app.api-key.hash-secret}")
    private String hashSecret;

    public String createApiKey(UUID projectId) {
        String rawKey = "RLS" + UUID.randomUUID();

        ApiKey apiKey = ApiKey.builder()
                .projectId(projectId)
                .keyHash(hash(rawKey))
                .isActive(Boolean.TRUE)
                .build();

        apiKeyRepository.save(apiKey);
        return rawKey;
    }

    public String getApiKeyForProject(UUID projectId) {
        return apiKeyRepository.findByProjectIdAndStatus(projectId, Boolean.TRUE)
                .map(ApiKey::getKeyHash)
                .orElseThrow(() -> new IllegalStateException("Active API key not found for project"));
    }

    public String rotateKey(UUID projectId) {
        apiKeyRepository.findByProjectIdAndStatus(projectId, Boolean.TRUE)
                .ifPresent(key -> {
                    key.setIsActive(Boolean.FALSE);
                    key.setRevokedAt(Instant.now());
                    apiKeyRepository.save(key);
                });

        return createApiKey(projectId);
    }

    private String hash(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec =
                    new SecretKeySpec(hashSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash API key", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
