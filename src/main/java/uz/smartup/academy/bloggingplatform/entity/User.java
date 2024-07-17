package uz.smartup.academy.bloggingplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "user")
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "bio", length = 400)
    private String bio;

    @Column(name = "registered")
    private LocalDate registered;

    @Column(name = "enabled")
    private String enabled ;

    @Column(name = "password", length = 100)
    private String password;

    @OneToMany(  cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "username", referencedColumnName = "username", updatable = false)
    private List<Role> roles;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public void addPostToAuthor(Post post){
        if(posts == null) posts = new ArrayList<>();
        posts.add(post);
        post.setAuthor(this);
    }
    public  void removeAuthorsPost(Post post){
        if(posts != null) posts.remove(post);
        post.setAuthor(null);
    }

    public void addRole(Role role) {
        if(roles.isEmpty()) {
            roles = new ArrayList<>();
        }

        roles.add(role);
    }
}
