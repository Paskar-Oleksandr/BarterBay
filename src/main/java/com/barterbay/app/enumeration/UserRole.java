package com.barterbay.app.enumeration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Set;

public enum UserRole {
  USER, ADMIN, MASTER;

  public Set<GrantedAuthority> getAuthorities() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return Collections.emptySet();
    }
    return Set.copyOf(authentication.getAuthorities());
  }
}
