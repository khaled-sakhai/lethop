package com.example.springsocial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.cloud.storage.Storage;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class WebConfig {

  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAwareImpl();
  }

 
}
