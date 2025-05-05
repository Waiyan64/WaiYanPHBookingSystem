// package com.example.RestService.security;

// import java.util.HashSet;

// import org.apache.coyote.BadRequestException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.RestService.common.record.UserDetailRecord;
// import com.example.RestService.constant.AuthUser;
// import com.example.RestService.constant.CustomerStatus;
// import com.example.RestService.entity.Customer;
// import com.example.RestService.repository.CustomerRepository;

// import lombok.AllArgsConstructor;

// @AllArgsConstructor
// @Service("customerDetailService")
// public class CustomerDetailService implements UserDetailsService {
    
//     private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDetailService.class);

//     private final CustomerRepository customerRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
//         LOGGER.info("CustomerDetailService.loadUserByUsername() username: {}", username );

//         Customer customer = this.customerRepository.findByPhoneNumber(username)
//                 .orElseThrow(() -> new UsernameNotFoundException(String.format("%s is not found.", username)));

//         if (customer.getStatus() == CustomerStatus.NEW) { 
//             throw new com.example.RestService.common.exception.BadRequestException("Not Found", "User Account is not registered.");
//         }

//         return CustomUserDetail.create(
//             new UserDetailRecord(
//                 customer.getId(), 
//                 customer.getPhoneNumber(), 
//                 customer.getPassword(), 
//                 AuthUser.CUSTOMER, 
//                 customer.getEnable(), 
//                 new HashSet<>(), 
//                 customer.getIsWalletUser())
//         );
//     }
// }
