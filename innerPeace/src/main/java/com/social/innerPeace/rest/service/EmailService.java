package com.social.innerPeace.rest.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public int sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            return 1; // 성공
        } catch (MailException exception) {
            // 이메일이 존재하지 않는 경우와 그 외의 오류를 구분
            if (exception.getMessage().contains("User does not exist")) {
                return 0; // 이메일이 존재하지 않음
            } else {
                return -1; // 그 외의 오류
            }
        }
    }
}