// package com.example.RestService.security;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.example.RestService.constant.AppConstant;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class ApiKeyFilter extends OncePerRequestFilter {
//     @Autowired
//     private IApiKeyAuthService apiKeyAuthService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         String apiKey = request.getHeader(AppConstant.API_TOKEN_HEADER_NAME);

//         if (StringUtils.hasText(apiKey) && this.apiKeyAuthService.validKey(apiKey)) {
//             Authentication authentication = this.apiKeyAuthService.getAuthentication(apiKey);
//             SecurityContextHolder.getContext().setAuthentication(authentication);
//         }

//         filterChain.doFilter(request, response);
//     }
// }
