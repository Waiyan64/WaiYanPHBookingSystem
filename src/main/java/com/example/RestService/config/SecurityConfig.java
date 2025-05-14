package com.example.RestService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
public AuthenticationManager authenticationManager(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
}
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/swagger-ui/**", 
                                     "/v3/api-docs/**", 
                                     "/auth/login", 
                                     "/auth/register", 
                                     "/auth/verify", 
                                     "/auth/test-token"
                                     ).permitAll();
                    authorize.requestMatchers("/api/packages/create").permitAll();
                    authorize.requestMatchers("/api/countries/**").permitAll();
                    authorize.requestMatchers("/api/classes/**").permitAll(); 
                    authorize.requestMatchers("/api/auth/password/change").authenticated();
                    authorize.requestMatchers("/api/packages").authenticated(); 
                    authorize.anyRequest().authenticated();

                });
                
    //             {
                    
    //                 authorize.requestMatchers("/api/auth/test-token").permitAll(); // Allow testing token without login
    //                 // Public endpoints
    //                 authorize.requestMatchers("/swagger-ui/**", 
    //                 "/v3/api-docs/**", 
    //                 "/swagger-ui.html", 
    //                 "/api-docs/**").permitAll();
    //                 authorize.requestMatchers("/api/auth/**").permitAll();
                    
    //                 authorize.requestMatchers("/v3/api-docs/**").permitAll();
    //                 authorize.requestMatchers("/actuator/**").permitAll();

    //             // Auth endpoints
    //             authorize.requestMatchers("/api/auth/**").permitAll();
                
    //             // Booking endpoints - require authentication
    //             authorize.requestMatchers("/api/bookings/classes").permitAll(); // Allow viewing classes without login
    //             authorize.requestMatchers("/api/bookings/classes/country/**").permitAll(); // Allow viewing classes by country
    //             authorize.requestMatchers("/api/bookings/classes/*/availability").permitAll(); // Allow checking availability
    //             authorize.requestMatchers("/api/bookings/**").authenticated();
                
    //             // Package endpoints - also require authentication
    //             authorize.requestMatchers("/api/packages").permitAll(); // Allow viewing packages without login
    //             authorize.requestMatchers("/api/packages/country/**").permitAll(); // Allow viewing by country
    //             authorize.requestMatchers("/api/packages/**").authenticated();
                
    //             // Admin endpoints - would require ADMIN role in a real implementation
    //             authorize.requestMatchers("/api/admin/**").authenticated(); // Simplified - should be role-based
                
    //             // Default - require authentication
    //             authorize.anyRequest().authenticated();
    // });
                

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();



    }
}
