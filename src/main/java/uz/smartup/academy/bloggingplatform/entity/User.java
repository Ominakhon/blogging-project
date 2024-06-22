package uz.smartup.academy.bloggingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Setter
    @Getter
    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Setter
    @Getter
    @Column(name = "username", length = 50)
    private String username;

    @Setter
    @Getter
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Setter
    @Getter
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Setter
    @Getter
    @Column(name = "email", length = 50)
    private String email;

    @Setter
    @Getter
    @Column(name = "bio", length = 400)
    private String bio;

    @Setter
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registered")
    private Date registered;

    @Setter
    @Getter
    @Column(name = "password", length = 50)
    private String password;

    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "username", referencedColumnName = "username", updatable = false)
    private Set<Role> roles;

}
