package uz.smartup.academy.bloggingplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager(dataSource);

        detailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM user WHERE username = ?");
        detailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM role WHERE username = ?");

        return detailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authManager ->  authManager
                                .requestMatchers(HttpMethod.GET, "/admin", "/admin/*").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/", "/register", "/posts/*", "/profile/*", "/categories/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/profile/*", "/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/photos/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/changePassword").authenticated()
                                .requestMatchers(HttpMethod.POST, "/password-change").authenticated()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form.loginPage("/login")
                                .loginProcessingUrl("/authenticate")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll()
                );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
