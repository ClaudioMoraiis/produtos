package com.example.demo.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecutiryConfigurations {
    @Autowired
    SecurityFilter fSecurityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity mHttpSecurity) throws Exception{
        return mHttpSecurity
                .cors(Customizer.withDefaults())
                .csrf(mCsrf -> mCsrf.disable())
                .sessionManagement(mSession -> mSession.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(mAuthorize -> mAuthorize
                        .requestMatchers(HttpMethod.POST, "usuario/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "usuario/register").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(fSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration mAuthenticationConfiguration) throws Exception{
        return mAuthenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
