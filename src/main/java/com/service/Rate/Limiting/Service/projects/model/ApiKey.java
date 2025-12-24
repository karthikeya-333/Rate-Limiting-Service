package com.service.Rate.Limiting.Service.projects.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_keys")
@Data
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private UUID projectId;

    @Column(nullable = false, unique = true, length = 128)
    private String keyHash;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant revokedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.isActive = Boolean.TRUE;
    }
}

