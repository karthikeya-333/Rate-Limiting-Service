package com.service.Rate.Limiting.Service.projects.repository;

import com.service.Rate.Limiting.Service.projects.model.RateLimit;
import com.service.Rate.Limiting.Service.projects.dto.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, UUID> {

    List<RateLimit> findByProjectId(UUID projectId);

    Optional<RateLimit> findByProjectIdAndDimensionAndWindow(UUID projectId, String dimension, Window window);

    List<RateLimit> findByProjectIdAndDimension(UUID projectId, String dimension);

    void deleteByProjectIdAndDimensionAndWindow(UUID projectId, String dimension, Window window);
}
