package uz.smartup.academy.bloggingplatform.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.dto.UserDtoUtil;
import uz.smartup.academy.bloggingplatform.entity.PasswordChangeForm;
import uz.smartup.academy.bloggingplatform.entity.PasswordResetToken;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.service.MailSenderService;
import uz.smartup.academy.bloggingplatform.service.UserService;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class UserMVC {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final UserDtoUtil userDtoUtil;

    public UserMVC(UserService userService, PasswordEncoder passwordEncoder, MailSenderService mailSenderService, UserDtoUtil userDtoUtil) {
        this.service = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
        this.userDtoUtil = userDtoUtil;
    }

    @GetMapping("/register")
    public String createUser(Model model) {
        model.addAttribute("user", new UserDTO());

        return "register";
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> createUser(@ModelAttribute("user") UserDTO user, HttpServletRequest request, HttpSession session) {
        if (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
            if(service.userExists(user.getUsername(), user.getEmail())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username or email is already taken"));
            }

            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setRole("ROLE_VIEWER");
            role.setUsername(user.getUsername());
            roles.add(role);
            service.registerUserWithConfirmation(user, roles);

            session.setAttribute("registrationMessage", "Registration successful. Check your email inbox and verify your email.");
            session.setAttribute("user", user);
            return ResponseEntity.ok(Collections.singletonMap("redirect", "/login"));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Invalid request"));
    }


    @GetMapping("/auto-login")
    public String autoLogin(HttpSession session, HttpServletRequest request) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        if (userDTO != null) {
            try {
                System.out.println(userDTO.getPassword());
                request.login(userDTO.getUsername(), userDTO.getPassword());
                return "redirect:/";
            } catch (ServletException e) {
                return "redirect:/login?error=true";
            }
        }

        System.out.println("login");
        return "redirect:/login";
    }




//    @PostMapping("/register-user")
//    public String createUser(Model model, @ModelAttribute("user") UserDTO user, RedirectAttributes attributes, HttpServletRequest request) {
//
//        if(service.userExists(user.getUsername(), user.getEmail())) {
//
//            System.out.println("-".repeat(100));
//            System.out.println("user already exists");
//            System.out.println("-".repeat(100));
//
//            model.addAttribute("error", "this username or email is already taken");
//            model.addAttribute("user", new UserDTO());
//            return "register";
//        }
//
//        List<Role> roles = new ArrayList<>();
//        Role role = new Role();
//        role.setRole("ROLE_VIEWER");
//        role.setUsername(user.getUsername());
//        roles.add(role);
//        service.registerUserWithConfirmation(user, roles);
//
//        attributes.addFlashAttribute("success", "Registration successful! A verification email has been sent.");
//        return "redirect:/login";
//    }



    @GetMapping("/email-confirmation")
    public String activation(@RequestParam("token") String token, Model model){
        PasswordResetToken verificationToken = mailSenderService.findByToken(token);
        if(verificationToken == null){
            model.addAttribute("message", "Your verification token is invalid!");

        }
        else{
            User user = verificationToken.getUser();
            if(user.getEnabled().equals("0")){
                if(verificationToken.isExpired()){
                    model.addAttribute("message", "Token is expired!");
                }
                else{
                    user.setEnabled("1");
                    UserDTO userDTO = userDtoUtil.toDTO(user);
                    service.updateUser(userDTO);
                    model.addAttribute("message", "Account is successfully activated!");
                }
            }
            else {
                model.addAttribute("message", "Your account is already activated!");
            }
        }
        return "activation";
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
    public String login(Model model, HttpSession session) {
        String message = (String) session.getAttribute("registrationMessage");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("registrationMessage");
        }
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
