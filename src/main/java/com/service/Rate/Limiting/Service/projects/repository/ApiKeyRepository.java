package com.service.Rate.Limiting.Service.projects.repository;

import com.service.Rate.Limiting.Service.projects.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    Optional<ApiKey> findByProjectIdAndStatus(UUID projectId, Boolean status);
}

