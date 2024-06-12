package com.identity.example.service.impl;

import com.identity.example.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  @Mock private UserDetails userDetails;

  @InjectMocks private JwtService jwtService;

  @BeforeEach
  void setUp() {
    when(userDetails.getUsername()).thenReturn("username");
  }

  @Test
  void shouldCreateToken() {
    String token = jwtService.generateToken(userDetails.getUsername(), JWT_TOKEN_VALIDITY);

    assertNotNull(token);
    assertEquals(userDetails.getUsername(), jwtService.extractUsername(token));
  }

  @Test
  void shouldValidateToken() {
    String token = jwtService.generateToken(userDetails.getUsername(), JWT_TOKEN_VALIDITY);

    boolean isValid = jwtService.validateToken(token, userDetails);

    assertTrue(isValid);
  }

  @Test
  void shouldNotValidateTokenWithWrongUsername() {
    when(userDetails.getUsername()).thenReturn("wrong_username");
    String token = jwtService.generateToken("username", JWT_TOKEN_VALIDITY);

    boolean isValid = jwtService.validateToken(token, userDetails);

    assertFalse(isValid);
  }

  @Test
  void shouldNotValidateExpiredToken() throws InterruptedException {
    String token = jwtService.generateToken(userDetails.getUsername(),1);
    Thread.sleep(2000);
    boolean isValid = jwtService.validateToken(token, userDetails);
    assertFalse(isValid);
  }
}
