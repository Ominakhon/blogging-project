package uz.smartup.academy.bloggingplatform.service;

import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.entity.PasswordResetToken;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.repository.PasswordResetTokenRepository;
import uz.smartup.academy.bloggingplatform.service.MailSenderService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
//    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final int EXPIRATION_TIME_IN_MINUTES = 60 * 24;

    public MailSenderServiceImpl(JavaMailSender mailSender,  PasswordResetTokenRepository passwordResetTokenRepository) {
        this.mailSender = mailSender;
//        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public String sendMail(uz.smartup.academy.bloggingplatform.entity.User user) {
        try {
            PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);
            if (existingToken != null) {
                passwordResetTokenRepository.delete(existingToken);
            }

            String resetLink = createPasswordResetTokenForUser(user);

            SimpleMailMessage simpleMailMessage = getSimpleMailMessage(user, resetLink);

            mailSender.send(simpleMailMessage);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private static SimpleMailMessage getSimpleMailMessage(uz.smartup.academy.bloggingplatform.entity.User user, String resetLink) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("abdullakhmirfayziev81@gmail.com");
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Reset Password Request");
        simpleMailMessage.setText("Please click on the link below to reset your password:\n\n" + resetLink + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nGreen White News Support");
        return simpleMailMessage;
    }
//
//    private static SimpleMailMessage getVerificationMessage(User user, String token){
//        token =
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom("abdullakhmirfayziev81@gmail.com");
//        simpleMailMessage.setTo(user.getEmail());
//
//    }




    @Override
    @Transactional
    public String createPasswordResetTokenForUser(User user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(EXPIRATION_TIME_IN_MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(uuid.toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(expiryDate);

        PasswordResetToken savedToken = passwordResetTokenRepository.save(resetToken);

        return generateResetLink(savedToken.getToken());
    }

    private String generateResetLink(String token) {
        String endpointUrl = "http://localhost:8080/password/reset";
        return endpointUrl + "/" + token;
    }

    private LocalDateTime calculateExpiryDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
    }

    @Override
    public boolean hasExpired(LocalDateTime expiryDate) {
        return LocalDateTime.now().isAfter(expiryDate);
    }


    @Override
    @Transactional
    public void save(User user, String token){
        PasswordResetToken verificationToken = new PasswordResetToken(token, user);
        verificationToken.setExpiryDate(calculateExpiryDate());
        passwordResetTokenRepository.save(verificationToken);
    }

    @Override
    public PasswordResetToken findByUser(User user) {
        return passwordResetTokenRepository.findByUser(user);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
}


