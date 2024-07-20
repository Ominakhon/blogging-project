package uz.smartup.academy.bloggingplatform.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.PasswordChangeForm;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.CustomUserDetailsService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserMVC {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;;

    public UserMVC(UserService userService, PasswordEncoder passwordEncoder) {
        this.service = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "createUser";
    }

    @PostMapping("/register-user")
    public String createUser(@ModelAttribute("user") UserDTO user, RedirectAttributes attributes) {
        try {
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setRole("ROLE_VIEWER");
            role.setUsername(user.getUsername());
            roles.add(role);

            service.registerUser(user, roles);

            attributes.addFlashAttribute("success", "User registered and logged in successfully.");
            return "redirect:/login";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "An error occurred during registration. Please try again.");
            return "redirect:/register";
        }
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        String photo = "";
        UserDTO userDTO = getLoggedUser() == null ? null : service.getUserByUsername(getLoggedUser().getUsername());
        if (userDTO != null) {
            photo = service.encodePhotoToBase64(userDTO.getPhoto());
        }

        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        model.addAttribute("photo", photo);
        model.addAttribute("loggedIn", getLoggedUser());
        return "changePassword";
    }

    @PostMapping("/password-change")
    public String changePassword(@ModelAttribute("passwordChangeForm") PasswordChangeForm form, Model model, Principal principal, RedirectAttributes attributes) {
        UserDTO loggedUser = service.getUserByUsername(principal.getName());

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            String photo = "";
            UserDTO userDTO = getLoggedUser() == null ? null : service.getUserByUsername(getLoggedUser().getUsername());
            if (userDTO != null) {
                photo = service.encodePhotoToBase64(userDTO.getPhoto());
            }
            model.addAttribute("error", "New password and confirm password do not match.");
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
            model.addAttribute("photo", photo);
            model.addAttribute("loggedIn", getLoggedUser());
            return "changePassword";
        }

        if (!passwordEncoder.matches(form.getOldPassword(), loggedUser.getPassword())) {
            String photo = "";
            UserDTO userDTO = getLoggedUser() == null ? null : service.getUserByUsername(getLoggedUser().getUsername());
            if (userDTO != null) {
                photo = service.encodePhotoToBase64(userDTO.getPhoto());
            }
            model.addAttribute("error", "Old password is incorrect.");
            model.addAttribute("passwordChangeForm", new PasswordChangeForm());
            model.addAttribute("photo", photo);
            model.addAttribute("loggedIn", getLoggedUser());
            return "changePassword";
        }

        service.changePassword(loggedUser.getUsername(), form.getNewPassword());
        attributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginUserController(Model model) {
        return "login";
    }

    private UserDetails getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return (UserDetails) principal;

        return null;
    }
}
