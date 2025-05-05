package com.example.RestService.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import com.example.RestService.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import com.example.RestService.entity.User;

@AllArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProviderImpl.class);

   @Value("${app.jwt.secret:defaultSecretKeyThatIsLongEnoughForHS512SignatureAlgorithm}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-minutes:60}")
    private long jwtExpirationMinutes;
    
    private final UserRepository userRepository;
    
    public JwtTokenProviderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   @Override
    public String generateToken(TokenPayload payload) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (jwtExpirationMinutes * 60 * 1000));
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(payload.getUsername())
                .claim("id", payload.getId())
                .claim("full_name", payload.getFullName())
                .claim("email_verified", payload.getEmailVerified())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public String generateToken(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        TokenPayload payload = new TokenPayload();
        payload.setId(user.getId());
        payload.setUsername(user.getEmail());
        payload.setEmailVerified(user.isEmailVerified());
        
                
        return generateToken(payload);
    }

   @Override
   public TokenPayload getPayloadFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parser().decryptWith(key)
               .build().parseEncryptedClaims(token).getPayload();

               TokenPayload payload = new TokenPayload();
               payload.setId(claims.get("id", Long.class));
               payload.setUsername(claims.getSubject());
               payload.setFullName(claims.get("full_name", String.class));
               payload.setEmailVerified(claims.get("email_verified", Boolean.class));
               
               return payload;
       
   }

   @Override
   public boolean validateToken(String token) {
       try {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Jwts.parser()
               .decryptWith(key)
               .build().parseEncryptedClaims(token);
           return true;
       } catch (ExpiredJwtException var1) {
           LOGGER.error("ValidateToken() Token Expired");
           LOGGER.error("", var1);
       } catch ( Exception e) {
           LOGGER.error("ValidateToken() Invalid Json Token");
           LOGGER.error("", e);
       }
       return false;
   }



//    private TokenPayload getPayload(Claims claims) {
//        if (claims.get(TOKEN_PAYLOAD) != null) {
//            return AppUtils.getObjectToJson(claims.get(TOKEN_PAYLOAD).toString(), TokenPayload.class);
//        }

//        return null;
//    }
}
