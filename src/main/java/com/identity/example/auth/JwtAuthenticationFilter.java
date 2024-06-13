package com.identity.example.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

  public static final String SECRET =
      "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

  public static final String AUTHORIZATION = "Authorization";
  public static final String BEARER = "Bearer";

  // 5 minutes validity
  public static final long JWT_TOKEN_VALIDITY_SECONDS = 5 * 60;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
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
    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
      String token = extractTokenFromHeader(authorizationHeader);
      String username = extractUsernameFromToken(token);
      if (username != null) {
        return Optional.of(Pair.of(token, username));
      }
    }
    return Optional.empty();
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    if (isTokenExpired(token)) {
      log.info("Token expired");
      return false;
    }
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()));
  }

  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username, JWT_TOKEN_VALIDITY_SECONDS);
  }

  public String generateToken(String username, long tokenValidity) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username, tokenValidity);
  }


  protected String createToken(Map<String, Object> claims, String username, long tokenValidity) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenValidity))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    try {
      return extractExpiration(token).before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private void processRequestWithToken(
      HttpServletRequest request, Pair<String, String> authenticationPair) {
    String token = authenticationPair.getLeft();
    String username = authenticationPair.getRight();

    boolean tokenValid = token != null;
    boolean contextAuthNull = SecurityContextHolder.getContext().getAuthentication() == null;

    if (tokenValid && contextAuthNull) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      Boolean validateToken = validateToken(token, userDetails);

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
      return extractUsername(token);
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
