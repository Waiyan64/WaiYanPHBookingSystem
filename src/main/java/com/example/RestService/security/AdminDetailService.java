// package com.example.RestService.security;
// import java.util.HashSet;
// import java.util.Set;
// import java.util.stream.Collectors;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.example.RestService.common.record.UserDetailRecord;
// import com.example.RestService.constant.AuthUser;
// import com.example.RestService.entity.Admin;
// import com.example.RestService.entity.Group;
// import com.example.RestService.entity.Permission;
// import com.example.RestService.repository.AdminRepository;

// import lombok.AllArgsConstructor;

// @Service("adminDetailService")
// @AllArgsConstructor
// public class AdminDetailService implements UserDetailsService {

//     private static final Logger LOGGER = LoggerFactory.getLogger(AdminDetailService.class);

//     private final AdminRepository adminRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
//         LOGGER.info("AdminDetailService.loadUserByUsername() username: {}", username );

//         Admin admin = this.adminRepository.findByUsername(username)
//                 .orElseThrow(() -> new UsernameNotFoundException(String.format("%s is not found.", username)));

//         Set<Permission> permissions = new HashSet<>(admin.getPermissions());    
//         permissions.addAll(this.getPermission(admin.getGroups()));



//         return CustomUserDetail.create(
//             new UserDetailRecord(
//                 admin.getId(),
//                 admin.getUsername(),
//                 admin.getPassword(),
//                 AuthUser.ADMIN,
//                 true,
//                 permissions.stream()
//                     .map(Permission::getPermissionType)
//                     .collect(Collectors.toSet()),
//                 null
//             )
//         );
//     }

//     private Set<Permission> getPermission(Set<Group> groups) {
//         Set<Permission> permissions = new HashSet<>();
//         for (Group gp :
//                 groups) {
//             permissions.addAll(gp.getPermissions());
//         }
//         return permissions;
//     }
// }
