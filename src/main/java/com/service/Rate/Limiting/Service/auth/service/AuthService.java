package com.service.Rate.Limiting.Service.auth.service;


import com.service.Rate.Limiting.Service.auth.model.Otp;
import com.service.Rate.Limiting.Service.auth.model.User;
import com.service.Rate.Limiting.Service.auth.repository.OtpRepository;
import com.service.Rate.Limiting.Service.auth.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final OtpRepository otpRepo;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    // REGISTER USER
    public String register(String name, String email, String password) {

        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User(null, name, email, encoder.encode(password), false);
        userRepo.save(user);

        String otp = generateOtp(email);
        emailService.sendOtp(email, otp);

        return "OTP sent to email";
    }

    // VERIFY OTP
    public String verifyOtp(String email, String code) {

        Otp otp = otpRepo.findByEmailAndCode(email, code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("OTP expired");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setActive(true);
        userRepo.save(user);

        return "User verified successfully";
    }

    // LOGIN
    public String login(String email, String password) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new IllegalStateException("User not verified");
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwt.generateToken(String.valueOf(user.getId()), user.getEmail());
    }

    // FORGOT PASSWORD
    public String forgot(String email) {

        userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String otp = generateOtp(email);
        emailService.sendOtp(email, otp);

        return "OTP sent for password reset";
    }

    // RESET PASSWORD
    public String resetPassword(String email, String otpCode, String newPass) {

        otpRepo.findByEmailAndCode(email, otpCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(encoder.encode(newPass));
        userRepo.save(user);

        return "Password updated";
    }

    // GENERATE OTP
    private String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        Otp entity = new Otp(null, email, otp, LocalDateTime.now().plusMinutes(10));
        otpRepo.save(entity);
        return otp;
    }
}
