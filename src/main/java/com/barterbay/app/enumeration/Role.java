package com.barterbay.app.enumeration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Set;

public enum Role {
  USER, ADMIN, MASTER;

  public Set<GrantedAuthority> getAuthorities() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return Collections.emptySet();
    }
    return Set.copyOf(authentication.getAuthorities());
  }
}

