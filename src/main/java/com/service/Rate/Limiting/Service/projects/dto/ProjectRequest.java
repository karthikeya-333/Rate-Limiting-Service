package com.service.Rate.Limiting.Service.projects.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class ProjectRequest {
    @NotBlank(message = "Project name is required and cannot be blank.")
    private String name;

    @NotBlank(message = "Description is required and cannot be blank.")
    private String description;
}

