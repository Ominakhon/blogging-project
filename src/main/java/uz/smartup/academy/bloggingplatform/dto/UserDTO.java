package uz.smartup.academy.bloggingplatform.dto;


import lombok.Getter;
import lombok.Setter;
import uz.smartup.academy.bloggingplatform.entity.Post;
import uz.smartup.academy.bloggingplatform.entity.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import uz.smartup.academy.bloggingplatform.service.LikeService;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private int id;
    private String first_name;
    private String last_name;
    private byte[] photo;
    private String email;
    private String bio;
    private String password;
    private String username;
    private LocalDate registered;
    private List<Role> roles;
    private List<Post> authorsPosts;

}
