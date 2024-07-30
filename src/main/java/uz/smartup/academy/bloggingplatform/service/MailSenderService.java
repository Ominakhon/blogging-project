package uz.smartup.academy.bloggingplatform.service;

import uz.smartup.academy.bloggingplatform.entity.PasswordResetToken;
import uz.smartup.academy.bloggingplatform.entity.User;

import java.time.LocalDateTime;

public interface MailSenderService {
    String sendMail(User user);
    String createPasswordResetTokenForUser(User user);


    boolean hasExpired(LocalDateTime expiryDate);

    void save(User user, String token);

    PasswordResetToken findByUser(User user);

    PasswordResetToken findByToken(String token);
}

