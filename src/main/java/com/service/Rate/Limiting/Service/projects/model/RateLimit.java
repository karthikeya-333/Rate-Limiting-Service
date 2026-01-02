package com.service.Rate.Limiting.Service.projects.model;

import com.service.Rate.Limiting.Service.projects.dto.RateLimitWindow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Entity
@Table(
        name = "rate_limits",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"projectId", "dimension", "window"})
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private String dimension;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RateLimitWindow rateLimitWindow;    // MINUTE, HOUR, DAY

    private int limitValue;

    private Instant createdAt;
    private Instant updatedAt;
}

