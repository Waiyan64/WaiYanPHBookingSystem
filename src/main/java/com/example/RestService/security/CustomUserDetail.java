// package com.example.RestService.security;

// import java.util.Collection;
// import java.util.Set;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.AuthorityUtils;
// import org.springframework.security.core.userdetails.UserDetails;

// import com.example.RestService.common.record.UserDetailRecord;
// import com.example.RestService.constant.AuthUser;

// import lombok.Getter;


// @Getter
// public class CustomUserDetail implements UserDetails {

//    private final Long id;
//    private final String username;

//    private final String password;

//    private final AuthUser userType;

//    private final Boolean enable;

//    private final Set<String> permissions;

//    private final Boolean isWalletUser;

//    private CustomUserDetail(UserDetailRecord userDetailRecord) {
//        this.id = userDetailRecord.id();
//        this.username = userDetailRecord.username();
       

//    }

//    public static CustomUserDetail create(UserDetailRecord userDetailRecord) {
//        return new CustomUserDetail(userDetailRecord);
//    }

//    public static CustomUserDetail create(TokenPayload tokenPayload) {
//        return new CustomUserDetail(new UserDetailRecord(
//            tokenPayload.getId(),
//             tokenPayload.getUsername()));
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.createAuthorityList(this.userType.toString());
//    }

//    @Override
//    public String getPassword() {
//        return this.password;
//    }

//    @Override
//    public String getUsername() {
//        return this.username;
//    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }

//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }

//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }

//    @Override
//    public boolean isEnabled() {
//        return this.enable;
//    }




// }
