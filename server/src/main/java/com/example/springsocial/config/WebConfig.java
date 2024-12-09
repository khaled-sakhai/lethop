package com.example.springsocial.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.springsocial.config.RateLimiter.RateLimitingInterceptor;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

import javax.sql.DataSource;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableCaching
public class WebConfig implements WebMvcConfigurer{

  private final long MAX_AGE_SECS = 3600;

    @Value("${app.cors.allowedOrigins}")
    private String[] allowedOrigins;

  @Bean
  public PasswordEncoder passwordEncoder() {


    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
    }

    
  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAwareImpl();
  }

  @Autowired
  private RateLimitingInterceptor rateLimitingInterceptor;



  @Bean
  public RateLimiter rateLimiter() {
    // allow 500 requests per 10 minutes, note: this is the total requests not user specific request.
      RateLimiterConfig config = RateLimiterConfig.custom()
              .limitForPeriod(500)
              .limitRefreshPeriod(Duration.ofSeconds(10))
              .timeoutDuration(Duration.ofMillis(100))
              .build();
      return RateLimiter.of("globalRateLimiter", config);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(rateLimitingInterceptor);
  }





}
