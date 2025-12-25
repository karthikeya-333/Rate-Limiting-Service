package com.service.Rate.Limiting.Service.projects.model;

import com.service.Rate.Limiting.Service.projects.dto.Window;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "rate_limits",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"projectId", "dimension", "window"})
        }
)
@Data
@Builder
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    private UUID projectId;

    @NotNull
    private String dimension;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Window window;    // MINUTE, HOUR, DAY

    private int limitValue;

    private Instant createdAt;
    private Instant updatedAt;
}

