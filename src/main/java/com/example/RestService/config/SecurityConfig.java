package com.example.RestService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.RestService.common.security.AuthenticationManagerBuilder;
import com.example.RestService.entity.User;
import com.example.RestService.repository.UserRepository;
import com.example.RestService.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserRepository userRepository;

  
    
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder();
    }

   @Bean
    public AuthenticationManager authenticationManager() { 
        return authentication -> {
            // This is a simplified authentication manager without customerDetailService
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
            
            if (!passwordEncoder().matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid email or password");
            }
            
            if (!user.isEnabled()) {
                throw new BadCredentialsException("Account is not activated");
            }
            
            return new UsernamePasswordAuthenticationToken(
                    user.getId().toString(), null, java.util.Collections.emptyList());
        };
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    // Public endpoints
                authorize.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll();
                
                // Auth endpoints
                authorize.requestMatchers("/api/auth/**").permitAll();
                
                // Booking endpoints - require authentication
                authorize.requestMatchers("/api/bookings/classes").permitAll(); // Allow viewing classes without login
                authorize.requestMatchers("/api/bookings/classes/country/**").permitAll(); // Allow viewing classes by country
                authorize.requestMatchers("/api/bookings/classes/*/availability").permitAll(); // Allow checking availability
                authorize.requestMatchers("/api/bookings/**").authenticated();
                
                // Package endpoints - also require authentication
                authorize.requestMatchers("/api/packages").permitAll(); // Allow viewing packages without login
                authorize.requestMatchers("/api/packages/country/**").permitAll(); // Allow viewing by country
                authorize.requestMatchers("/api/packages/**").authenticated();
                
                // Admin endpoints - would require ADMIN role in a real implementation
                authorize.requestMatchers("/api/admin/**").authenticated(); // Simplified - should be role-based
                
                // Default - require authentication
                authorize.anyRequest().authenticated();
    });

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();



    }
}
