package com.service.Rate.Limiting.Service.projects.repository;

import com.service.Rate.Limiting.Service.projects.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByProjectIdAndIsActive(Long projectId, Boolean status);

    Optional<ApiKey> findByKeyHash(String apiKey);

    Optional<ApiKey> findByProjectIdAndKeyHashAndIsActive(
            Long projectId,
            String keyHash,
            Boolean isActive
    );

}

