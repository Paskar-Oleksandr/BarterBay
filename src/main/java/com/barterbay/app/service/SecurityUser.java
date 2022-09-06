package com.barterbay.app.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SecurityUser implements UserDetails {

  private final String userName;
  private final String password;
  private final List<SimpleGrantedAuthority> authorities;
  private final boolean isActive;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isActive;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isActive;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }

  public static UserDetails fromUser(com.barterbay.app.domain.User user) {
    return new org.springframework.security.core.userdetails.User(
      user.getEmail(), user.getPassword(),
      user.getUserRole().getAuthorities()
    );
  }
}
