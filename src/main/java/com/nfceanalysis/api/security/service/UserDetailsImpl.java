package com.nfceanalysis.api.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nfceanalysis.api.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String email;

    private String name;

    @JsonIgnore
    private String password;

    private final Set<GrantedAuthority> authorities = new HashSet<>();

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserDetailsImpl(String id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
