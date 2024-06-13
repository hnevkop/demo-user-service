package com.identity.example.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

  private final String USERNAME = "username";

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private UserDetails userDetails;

  @Mock private UserDetailsService userDetailsService;

  @InjectMocks private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  void setup() {
    jwtAuthenticationFilter = new JwtAuthenticationFilter(userDetailsService);
  }

  @Test
  void shouldCreateAndValidateToken() {
    when(userDetails.getUsername()).thenReturn(USERNAME);
    String token = jwtAuthenticationFilter.generateToken(USERNAME);
    assertNotNull(token);
    assertEquals(USERNAME, jwtAuthenticationFilter.extractUsername(token));
    boolean isValid = jwtAuthenticationFilter.validateToken(token, userDetails);
    assertTrue(isValid);
  }

  @Test
  void shouldNotValidateTokenWithWrongUsername() {
    when(userDetails.getUsername()).thenReturn("wrong_username");
    String token = jwtAuthenticationFilter.generateToken(USERNAME);
    boolean isValid = jwtAuthenticationFilter.validateToken(token, userDetails);
    assertFalse(isValid);
  }

  @Test
  void shouldNotValidateExpiredToken() throws InterruptedException {
    String token = jwtAuthenticationFilter.generateToken(userDetails.getUsername(), 1);
    Thread.sleep(2000);
    boolean isValid = jwtAuthenticationFilter.validateToken(token, userDetails);
    assertFalse(isValid);
  }

  @Test
  void shouldReturnEmptyPairWhenNoAuthorizationHeader() {
    when(request.getHeader(JwtAuthenticationFilter.AUTHORIZATION)).thenReturn(null);
    Optional<Pair<String, String>> optional = jwtAuthenticationFilter.extractTokenAndUser(request);
    assertFalse(optional.isPresent());
  }

  @Test
  void shouldReturnEmptyPairWhenAuthorizationHeaderIsNotStartingWithBearer() {
    when(request.getHeader(JwtAuthenticationFilter.AUTHORIZATION)).thenReturn("NonBearer token");
    Optional<Pair<String, String>> optional = jwtAuthenticationFilter.extractTokenAndUser(request);
    assertFalse(optional.isPresent());
  }

  @Test
  void shouldReturnOptionalPairWhenAuthorizationHeaderIsCorrect() {

    // token - no expiration
    String expectedToken =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzE4MjkwNjg3fQ._jGcvNXSmMpJQNdoF7HWFDByHT_qoBb-iAdIBqYkMYA";
    String expectedUsername = "user";

    when(request.getHeader(JwtAuthenticationFilter.AUTHORIZATION))
        .thenReturn("Bearer " + expectedToken);

    Optional<Pair<String, String>> optional = jwtAuthenticationFilter.extractTokenAndUser(request);

    assertTrue(optional.isPresent());
    assertEquals(expectedToken, optional.get().getLeft());
    assertEquals(expectedUsername, optional.get().getRight());
  }
}
