package uz.smartup.academy.bloggingplatform.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.PasswordChangeForm;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserMVC {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserMVC(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.service = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "createUser";
    }

    @PostMapping("/register-user")
    public String createUser(Model model, @ModelAttribute("user") UserDTO user, RedirectAttributes attributes, HttpServletRequest request) {
        try {
//            if (service.getUserByUsername(user.getUsername()) != null) {
//                model.addAttribute("error", "this username already exist, please use another username");
//                model.addAttribute("user", new UserDTO());
//                return "createUser";
//            }

            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setRole("ROLE_VIEWER");
            role.setUsername(user.getUsername());
            roles.add(role);

            service.registerUser(user, roles);

            request.login(user.getUsername(), user.getPassword());

            attributes.addFlashAttribute("success", "User registered and logged in successfully.");
            return "redirect:/";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "An error occurred during registration. Please try again.");
            return "redirect:/register";
        }
    }

    public void autoLogin(String username, String password, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //this step is important, otherwise the new login is not in session which is required by Spring Security
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
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

    @GetMapping("/complete-registration")
    public String showCompleteRegistrationPage(Model model, HttpSession session) {
        OAuth2User oauth2User = (OAuth2User) session.getAttribute("oauth2User");
        model.addAttribute("email", oauth2User.getAttribute("email"));
        return "setPassword";
    }

    @PostMapping("/complete-registration")
    public String completeRegistration(@RequestParam String email,
                                       @RequestParam String username,
                                       @RequestParam String password,
                                       @RequestParam String confirmPassword,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session,
                                       HttpServletRequest request) throws ServletException {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/complete-registration";
        }

        OAuth2User oauth2User = (OAuth2User) session.getAttribute("oauth2User");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");

        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRole("ROLE_VIEWER");
        role.setUsername(username);
        roles.add(role);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setFirst_name(firstName);
        userDTO.setLast_name(lastName);
        userDTO.setPassword(password);
        userDTO.setEmail(email);

        service.registerUser(userDTO, roles);

        Authentication auth = new UsernamePasswordAuthenticationToken(username, service.getUserByUsername(username).getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        request.login(username, password);

        return "redirect:/";
    }

}
