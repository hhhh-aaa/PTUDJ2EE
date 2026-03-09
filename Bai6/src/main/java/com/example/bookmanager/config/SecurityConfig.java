package com.example.bookmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Simple security configuration that secures the `/books` endpoints.
 * <p>
 * For demonstration purposes we use an in-memory user store with two users:
 * <ul>
 *   <li><strong>admin</strong> / <code>123456</code> (ROLE_ADMIN)</li>
 *   <li><strong>user</strong> / <code>123456</code> (ROLE_USER)</li>
 * </ul>
 *
 * The administrator has full CRUD rights whereas the regular user is limited
 * to read-only operations (list + search).
 */
@Configuration
@EnableMethodSecurity // allow @PreAuthorize etc. on controllers if needed
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                    // writing/updating and deletion only for admins
                    .requestMatchers("/books/new", "/books/edit/**", "/books/delete/**").hasRole("ADMIN")
                    .requestMatchers(org.springframework.http.HttpMethod.POST, "/books/**").hasRole("ADMIN")
                    // read/search endpoints require authentication
                    .requestMatchers("/books/**").authenticated()
                    .anyRequest().permitAll()
            )
            .formLogin()          // default login page
            .and()
            .logout();
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123456")
                .roles("ADMIN")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}