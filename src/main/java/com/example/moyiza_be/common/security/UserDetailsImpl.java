//package com.example.moyiza_be.common.security;
//
//import com.example.moyiza_be.common.enums.UserRoleEnum;
//import com.example.moyiza_be.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Component
//public class UserDetailsImpl implements UserDetails {
//    private final User user;
//
//    public UserDetailsImpl(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRoleEnum role = UserRoleEnum.ROLE_USER;
//        String authority = role.getAuthorization();
//
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        System.out.println("user.getPassword() = " + user.getPassword());
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() { return this.user.getEmail(); }
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
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public User getUser() {
//        return this.user;
//    }
//}
