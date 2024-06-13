package com.identity.example.configuration;

import com.identity.example.auth.JwtAuthenticationEntryPoint;
import com.identity.example.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtAuthenticationFilter filter;

  public SecurityConfig(
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter filter) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.filter = filter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/webjars/**")
                    .permitAll()
                    .requestMatchers("/auth/login")
                    .permitAll()
                    .requestMatchers("/users/**")
                    // .permitAll() - testing purposes
                    .authenticated()
                    .requestMatchers("/projects/**")
                    // .permitAll() - testing purposes
                    .authenticated()
                    .requestMatchers("/actuator/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
