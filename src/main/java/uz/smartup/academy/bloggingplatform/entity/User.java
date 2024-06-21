package uz.smartup.academy.bloggingplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "username")
    private String username;

    @Setter
    @Getter
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @Getter
    @Column(name = "last_name")
    private String lastName;

    @Setter
    @Getter
    @Column(name = "email")
    private String email;

    @Setter
    @Getter
    @Column(name = "bio")
    private String bio;

    @Setter
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registered")
    private Data registered;

    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "username", referencedColumnName = "username", updatable = false)
    private Set<Role> roles;

}
