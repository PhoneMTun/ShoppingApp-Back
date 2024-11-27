package com.bfs.hibernateprojectdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bfs.hibernateprojectdemo.security.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtFilter jwtFilter;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Password encoder bean to use BCrypt for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Main security configuration for the application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable() 
            .authorizeRequests()
            .antMatchers("/signup", "/login").permitAll() // Allow public access to signup and login
            .antMatchers("/products/**", "/orders/**", "/watchlist/**", "/admin/**").authenticated() // Authenticated access
            .antMatchers("/admin/**").hasRole("ADMIN") // Admin access
            .anyRequest().authenticated() // Any other request requires authentication
            .and()
            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class); 

        return http.build();
    }
}
