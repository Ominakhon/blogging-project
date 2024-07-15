package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.entity.User;

import java.time.LocalDateTime;

public interface MailSenderService {
    String sendMail(User user);
    String createPasswordResetTokenForUser(User user);


    boolean hasExpired(LocalDateTime expiryDate);
}

