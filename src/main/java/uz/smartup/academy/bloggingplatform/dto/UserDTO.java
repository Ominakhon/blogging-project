package uz.smartup.academy.bloggingplatform.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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


}
