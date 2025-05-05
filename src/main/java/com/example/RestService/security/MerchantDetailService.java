// package com.example.RestService.security;

// import java.util.HashSet;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.RestService.common.record.UserDetailRecord;
// import com.example.RestService.constant.AuthUser;
// import com.example.RestService.entity.Merchant;
// import com.example.RestService.repository.MerchantRepository;

// import lombok.AllArgsConstructor;

// @AllArgsConstructor
// @Service("merchantDetailService")
// public class MerchantDetailService implements UserDetailsService {

//     private static final Logger LOGGER = LoggerFactory.getLogger(MerchantDetailService.class);

//     private final MerchantRepository merchantRepository;

//     @Override 
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
//         LOGGER.info("MerchantDetailService.loadUserByUsername() username: {}", username);

//         Merchant merchant = this.merchantRepository.findByUsername(username)
//                 .orElseThrow(() -> new UsernameNotFoundException(String.format("%s is not found.", username)));

//         return CustomUserDetail.create(
//             new UserDetailRecord(merchant.getId(), 
//             merchant.getUsername(), 
//             merchant.getPassword(), 
//             AuthUser.MERCHANT,
//             merchant.getEnable(),
//             new HashSet<>(),
//             null)
//         );

//     }
    
// }
