package com.identity.example.auth;

import com.identity.example.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION = "Authorization";
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Optional<Pair<String, String>> optPair = extractTokenAndUser(request);
    optPair.ifPresent(stringStringPair -> processRequestWithToken(request, stringStringPair));
    filterChain.doFilter(request, response);
  }

  protected Optional<Pair<String, String>> extractTokenAndUser(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
      String token = extractTokenFromHeader(authorizationHeader);
      String username = extractUsernameFromToken(token);
      if (username != null) {
        return Optional.of(Pair.of(token, username));
      }
    }
    return Optional.empty();
  }

  private void processRequestWithToken(
      HttpServletRequest request, Pair<String, String> authenticationPair) {
    String token = authenticationPair.getLeft();
    String username = authenticationPair.getRight();

    boolean tokenValid = token != null;
    boolean contextAuthNull = SecurityContextHolder.getContext().getAuthentication() == null;

    if (tokenValid && contextAuthNull) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Boolean validateToken = jwtService.validateToken(token, userDetails);

      if (validateToken) {
        setupSecurityContext(request, userDetails);
      } else {
        logger.warn("Validation fails!");
      }
    }
  }

  private void setupSecurityContext(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String extractTokenFromHeader(String authorizationHeader) {
    return authorizationHeader.substring(7);
  }

  private String extractUsernameFromToken(String token) {
    try {
      return jwtService.extractUsername(token);
    } catch (IllegalArgumentException e) {
      log.error("Illegal Argument while fetching the username!", e);
    } catch (ExpiredJwtException e) {
      log.error("Given JWT token is expired!", e);
    } catch (MalformedJwtException e) {
      log.error("Token malformed! Invalid Token", e);
    } catch (Exception e) {
      log.error("Exception occurred while fetching the username from token!", e);
    }
    return null;
  }
}
