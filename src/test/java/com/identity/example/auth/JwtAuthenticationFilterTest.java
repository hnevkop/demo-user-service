package com.identity.example.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.identity.example.service.JwtService;
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
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
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
        String expectedToken = "token";
        String expectedUsername = "username";
        when(request.getHeader(JwtAuthenticationFilter.AUTHORIZATION)).thenReturn("Bearer " + expectedToken);
        when(jwtService.extractUsername(expectedToken)).thenReturn(expectedUsername);

        Optional<Pair<String, String>> optional = jwtAuthenticationFilter.extractTokenAndUser(request);

        assertTrue(optional.isPresent());
        assertEquals(expectedToken, optional.get().getLeft());
        assertEquals(expectedUsername, optional.get().getRight());
    }


}
