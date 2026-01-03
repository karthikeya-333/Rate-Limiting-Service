package com.service.Rate.Limiting.Service.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Your OTP Code");
        msg.setText("Your OTP is: " + otp);

        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            log.error("e: ", ex);
            throw new IllegalStateException("Unable to send email at the moment");
        }
    }
}
