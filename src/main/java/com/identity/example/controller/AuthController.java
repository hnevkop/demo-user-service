package com.identity.example.controller;

import com.identity.example.auth.JwtAuthenticationFilter;
import com.identity.example.auth.JwtRequest;
import com.identity.example.auth.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;

  public AuthController(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      UserDetailsService userDetailsService,
      AuthenticationManager authenticationManager) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

    // Auth user and generate token
    doAuthenticate(request.user(), request.password());
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.user());
    String token = jwtAuthenticationFilter.generateToken(userDetails.getUsername());

    JwtResponse response = new JwtResponse(token, userDetails.getUsername());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void doAuthenticate(String email, String password) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(email, password);
    try {
      authenticationManager.authenticate(authentication);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid Username or Password!");
    }
  }
}
