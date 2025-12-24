package com.service.Rate.Limiting.Service.auth.controller;


import com.service.Rate.Limiting.Service.auth.dto.AuthDTO;
import com.service.Rate.Limiting.Service.auth.service.AuthService;
import com.service.Rate.Limiting.Service.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody AuthDTO.RegisterRequest r) {
        String result = auth.register(r.getName(), r.getEmail(), r.getPassword());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Registration successful, OTP sent")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verify(@Valid @RequestBody AuthDTO.OtpRequest r) {
        String result = auth.verifyOtp(r.getEmail(), r.getOtp());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("OTP verified")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody AuthDTO.LoginRequest r) {
        String token = auth.login(r.getEmail(), r.getPassword());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Login successful")
                        .data(token)
                        .build()
        );
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<String>> forgot(@Valid @RequestBody AuthDTO.ForgotPasswordRequest r) {
        String res = auth.forgot(r.getEmail());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("OTP sent to email")
                        .data(res)
                        .build()
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> reset(@Valid @RequestBody AuthDTO.ResetPasswordRequest r) {
        String res = auth.resetPassword(r.getEmail(), r.getOtp(), r.getNewPassword());
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Password reset successful")
                        .data(res)
                        .build()
        );
    }
}
