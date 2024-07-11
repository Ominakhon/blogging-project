package uz.smartup.academy.bloggingplatform.mvc;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserMVC {

    private final UserService service;

    private final PasswordEncoder passwordEncoder;

    public UserMVC(UserService userService, PasswordEncoder passwordEncoder) {
        this.service = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String createInstructor(Model model){
        model.addAttribute("user", new UserDTO());
        return "createUser";
    }

    @PostMapping("/register")
    public String createInstructor(@ModelAttribute("user") UserDTO dto){
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRole("ROLE_VIEWER");
        role.setUsername(dto.getUsername());
        roles.add(role);
        service.registerUser(dto, roles);
        return "redirect:/login";
    }



    @GetMapping("/login")
    public String LoginUserController(Model model){
        return "login";
    }
}
