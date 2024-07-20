package uz.smartup.academy.bloggingplatform.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.dto.UserDTO;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.service.CustomUserDetailsService;
import uz.smartup.academy.bloggingplatform.service.UserService;

import java.io.IOException;
import java.util.*;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    private final UserDao userDao;

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public OAuth2LoginSuccessHandler(@Lazy UserService userService, @Lazy UserDao userDao, @Lazy CustomUserDetailsService userDetailsService,@Lazy PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDao = userDao;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = token.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");


        User user = null;
        if (userDao.getUserByEmail(email) != null) {
            user = userDao.getUserByEmail(email);
        } else {
            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setRole("ROLE_VIEWER");
            role.setUsername(firstName);
            roles.add(role);

            String randomPassword = UUID.randomUUID().toString();

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(firstName.toLowerCase(Locale.ROOT));
            userDTO.setFirst_name(firstName);
            userDTO.setLast_name(lastName);
            userDTO.setPassword(randomPassword);
            userDTO.setEmail(email);

            userService.registerUser(userDTO, roles);
        }

        // Manually log in the user
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

