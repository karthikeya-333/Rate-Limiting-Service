package com.service.Rate.Limiting.Service.rateService;

import com.service.Rate.Limiting.Service.projects.dto.RateLimitWindow;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "api_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private String dimension;

    @Enumerated(EnumType.STRING)
    private RateLimitWindow rateLimitWindow;     // MINUTE, HOUR, DAY

    private int requestCount;

    private LocalDate usageDate;

    private Instant createdAt;
}

