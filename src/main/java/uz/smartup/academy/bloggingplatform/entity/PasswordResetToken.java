package uz.smartup.academy.bloggingplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetToken {

    private static final int EXPIRATION = 160 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    private LocalDateTime expiryDate;

//    public PasswordResetToken() {}
//
//    public PasswordResetToken(String token, User user) {
//        this.token = token;
//        this.user = user;
//        this.expiryDate = calculateExpiryDate();
//    }
//
//    private LocalDateTime calculateExpiryDate() {
//        return LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION);
//    }
//
//    public boolean isExpired() {
//        return LocalDateTime.now().isAfter(this.expiryDate);
//    }
}
