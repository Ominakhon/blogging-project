package uz.smartup.academy.bloggingplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.smartup.academy.bloggingplatform.dao.UserDao;
import uz.smartup.academy.bloggingplatform.entity.Role;
import uz.smartup.academy.bloggingplatform.entity.User;
import uz.smartup.academy.bloggingplatform.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=".repeat(100));
        System.out.println(email);
        System.out.println("=".repeat(100));

//        User user = userDao.findByEmail(email);
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(), user.getPassword(), user.getRoles().stream()
//                .map(role -> (GrantedAuthority) role::getRole).collect(Collectors.toList()));

        return new org.springframework.security.core.userdetails.User(
                "a", "1",
                List.of((GrantedAuthority) () -> "admin"));
    }
}
