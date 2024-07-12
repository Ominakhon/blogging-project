package uz.smartup.academy.bloggingplatform.mvc;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.PasswordChangeForm;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.security.Principal;
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
    public String createInstructor(@ModelAttribute("user") UserDTO dto, RedirectAttributes attributes){
        try {
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setRole("ROLE_VIEWER");
            role.setUsername(dto.getUsername());
            roles.add(role);

            service.registerUser(dto, roles);
            attributes.addFlashAttribute("success", "User registered successfully.");
            return "redirect:/login";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "An error occurred during registration. Please try again.");
            return "redirect:/register";
        }
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        return "changePassword";
    }

    @PostMapping("/password-change")
    public String changePassword(@ModelAttribute("passwordChangeForm") PasswordChangeForm form, Model model, Principal principal, RedirectAttributes attributes) {
        UserDTO loggedUser = service.getUserByUsername(principal.getName());

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "New password and confirm password do not match.");
            return "changePassword";
        }

        if (!passwordEncoder.matches(form.getOldPassword(), loggedUser.getPassword())) {
            model.addAttribute("error", "Old password is incorrect.");
            return "changePassword";
        }

        service.changePassword(loggedUser.getUsername(), form.getNewPassword());
        attributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/";
    }



    @GetMapping("/login")
    public String LoginUserController(Model model){
        return "login";
    }

    private UserDetails getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) principal;

        return null;
    }
}
