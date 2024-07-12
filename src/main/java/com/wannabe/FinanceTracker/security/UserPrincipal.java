package com.wannabe.FinanceTracker.security;

import com.wannabe.FinanceTracker.model.Role;
import com.wannabe.FinanceTracker.model.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {
    @Getter
    private final UUID id;

    @Getter
    private final String email;

    private final String username;

    private final String password;

    @Getter
    private final Boolean emailVerified;

    @Getter
    private final Boolean active;

    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    @Setter
    @Getter
    private Map<String, Object> attributes;

    public UserPrincipal(UUID id, String email, String username, String password, Boolean emailVerified, Boolean active, Collection<Role> authorities) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.emailVerified = emailVerified;
        this.active = active;
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.isEmailVerified(),
                user.isActive(),
                user.getRoles()
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
