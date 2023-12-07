package com.example.springsocial.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

// generate createdby - created at ..automatically..
public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    //  should get userName from spring security
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication authentication = context.getAuthentication();

    if (authentication != null) {
      return Optional.of(authentication.getName());
    } else {
      return Optional.of("Non-logged-in-User");
    }
    // for testing
    //return Optional.of("test user");
  }
}
