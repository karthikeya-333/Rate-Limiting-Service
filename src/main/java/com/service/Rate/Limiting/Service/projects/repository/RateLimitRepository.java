package com.service.Rate.Limiting.Service.projects.repository;

import com.service.Rate.Limiting.Service.projects.model.RateLimit;
import com.service.Rate.Limiting.Service.projects.dto.RateLimitWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, Long> {

    List<RateLimit> findByProjectId(Long projectId);

    Optional<RateLimit> findByProjectIdAndDimensionAndRateLimitWindow(Long projectId, String dimension, RateLimitWindow rateLimitWindow);

    List<RateLimit> findByProjectIdAndDimension(Long projectId, String dimension);

    void deleteByProjectIdAndDimensionAndRateLimitWindow(Long projectId, String dimension, RateLimitWindow rateLimitWindow);
}
