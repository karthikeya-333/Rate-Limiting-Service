package com.service.Rate.Limiting.Service.auth.repository;

import com.service.Rate.Limiting.Service.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
