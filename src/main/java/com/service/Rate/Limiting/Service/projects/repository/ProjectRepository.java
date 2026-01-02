package com.service.Rate.Limiting.Service.projects.repository;

import com.service.Rate.Limiting.Service.projects.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUserId(Long userId);

    Optional<Project> findByIdAndUserId(Long id, Long userId);

    Optional<Project> findByUserIdAndActive(Long userId, Boolean isActive);
}

