package com.identity.example.controller;

import com.identity.example.auth.JwtRequest;
import com.identity.example.auth.JwtResponse;
import com.identity.example.service.JwtService;
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

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final AuthenticationManager authenticationManager;

  public AuthController(
      JwtService jwtService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

    // Auth user and generate token
    doAuthenticate(request.user(), request.password());
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.user());
    String token = jwtService.generateToken(userDetails.getUsername(), JWT_TOKEN_VALIDITY);

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
