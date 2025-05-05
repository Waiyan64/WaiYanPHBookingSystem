package com.example.RestService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.RestService.web.response.ApiResponse;
import com.example.RestService.web.response.JwtAuthResponse;
import com.example.RestService.web.response.UserResponse;
import com.example.RestService.entity.User;
import com.example.RestService.security.JwtTokenProvider;
import com.example.RestService.service.UserService;
import com.example.RestService.web.request.LoginRequest;
import com.example.RestService.web.request.PasswordChangeRequest;
import com.example.RestService.web.request.PasswordResetRequest;
import com.example.RestService.web.request.RegistrationRequest;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/auth")
public class AuthController {
   // private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

   // private final IAuthenticationService authenticationService;

   // private final MessageSource messageSource;

   // @PostMapping(value = "/customer/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   // public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginRequest loginRequest) { 
   //    LOGGER.info("loginCustomer() loginRequest.username: {}", loginRequest.getUsername());

   //    try { 
   //       AuthResponseRecord authResponseRecord = this.authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword(), AuthUser.CUSTOMER);
   //       Map<String, Object> data = new HashMap<>();
   //       data.put("user_info", authResponseRecord);

   //       return HttpResponse.success( AppConstant.SUCCESS_MESSAGE, data);
   //    } catch (BadCredentialsException e) { 
   //       LOGGER.error("loginCustomer() message: {}",  e.getMessage());
   //       throw new UnauthorizedException(
   //          this.messageSource.getMessage("error.customerLogin.WrongPin.title", null, LocaleContextHolder.getLocale()),
   //          this.messageSource.getMessage("error.customerLogin.WrongPin.content",  null, LocaleContextHolder.getLocale())
      
   //       );
   //    }
   // }
   private final UserService userService;
   private final AuthenticationManager authenticationManager;
   private final JwtTokenProvider tokenProvider;

   
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        User user = userService.registerUser(request);
        return new ResponseEntity<>(
                new ApiResponse(true, "User registered successfully. Please check email for verification."),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        User user = userService.getUserByEmail(request.getEmail());
        
        return ResponseEntity.ok(new JwtAuthResponse(jwt, new UserResponse(user)));
    }
    
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully"));
    }
   
    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody PasswordChangeRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        
        userService.changePassword(user.getId(), request);
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
    }
    
    @PostMapping("/password/reset/request")
    public ResponseEntity<ApiResponse> requestPasswordReset(@RequestParam("email") String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok(
                new ApiResponse(true, "Password reset email sent successfully"));
    }
    
    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }
}
