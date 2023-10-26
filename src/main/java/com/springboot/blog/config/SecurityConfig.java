package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter){
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{ //securityFilterChain provides default login form
        http.csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((authorize)->
                        //authorize.anyRequest().authenticated())
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                //.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
/*    @Bean
    public UserDetailsService userDetailService(){
        UserDetails nemo = User.builder()
                .username("nemo")
                .password(passwordEncoder().encode("nemo"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(nemo, admin);
    }*/
}

/*

Of course! Let's break down that code into simpler terms:

Imagine your web application as a private club. This piece of code, SecurityConfig, sets the rules for who can get in.

The Rule Book: The class SecurityConfig is like the rule book that the bouncer (security) checks.

No Special Handshakes Allowed: Normally, there's a special handshake (called CSRF protection) that makes sure only legitimate members can get into the club. But this club is a bit relaxed and says, "No need for that handshake!" This is what the http.csrf((csrf)->csrf.disable()) part does.

Show Your ID: Before anyone gets in, they need to show their ID card. If they don’t have an ID, they can’t get in. This is what the .authorizeHttpRequests((authorize)-> authorize.anyRequest().authenticated()) part does. It's saying, "Anyone who wants to come in, please show your ID."

Basic ID Check: There are many ways to check IDs. This club has chosen the simplest way, which is just looking at the ID and not scanning it or anything. This simple check is what .httpBasic(Customizer.withDefaults()) means.

Ready to Go: Once the bouncer has these rules in hand, they're ready to stand at the door and start checking. That's the return http.build(); part.

In even simpler terms: This code is like a rule book for a club's security. It says, "No special handshakes. Just show a basic ID, and you can get in."
 */

/* @Bean
The @Bean annotation is like telling the clubhouse management: "Use THIS machine for codes, THIS rule book for the guard, and THIS list for members." Without @Bean, the management wouldn't know to use them, and things might not work correctly or securely.
 */

/*
In-Memory Authentication vs. application.properties
While both methods can store usernames and passwords, they serve different purposes. In-memory authentication is tailored for application user management, whereas application.properties is for general configuration and may store credentials for specific services.
 */