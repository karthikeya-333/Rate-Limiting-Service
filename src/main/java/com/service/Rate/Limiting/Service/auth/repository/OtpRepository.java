package com.service.Rate.Limiting.Service.auth.repository;

import com.service.Rate.Limiting.Service.auth.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndCode(String email, String code);
}
