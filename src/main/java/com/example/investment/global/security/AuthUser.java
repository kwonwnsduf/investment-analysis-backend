//package com.example.investment.global.security;
//
//
//import com.example.investment.domain.user.User;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@Getter
//public class AuthUser implements UserDetails {
//
//    private final Long id;
//    private final String email;
//    private final String password;
//    private final Collection<? extends GrantedAuthority> authorities;
//
//    public AuthUser(User user) {
//        this.id = user.getId();
//        this.email = user.getEmail();
//        this.password = user.getPasswordHash();
//        this.authorities = List.of(); // Role 쓰면 여기에 추가
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
