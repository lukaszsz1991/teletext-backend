package pl.studia.teletext.teletext_backend.config.middleware;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.studia.teletext.teletext_backend.domain.services.security.JwtService;
import pl.studia.teletext.teletext_backend.exceptions.JwtValidatingException;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
    log.debug("Checking request for JWT token");
    try {
      var token = extractToken(request);
      if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        log.debug("Authentication not found in context, processing token");
        jwtService.validateToken(token);
        var username = jwtService.extractUsername(token);
        authenticateUser(username, request);
      }
    } catch (JwtException | JwtValidatingException ex) {
      log.debug("JWT validation failed: {}", ex.getMessage());
    } catch (Exception ex) {
      log.error("Unexpected error in JwtAuthFilter", ex);
    }
    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String header = request.getHeader(AUTH_HEADER);
    if (!isAuthHeaderValid(header)) {
      log.debug("JWT token not found in request header");
      return null;
    }
    log.debug("JWT token found in request header");
    return header.substring(BEARER_PREFIX.length());
  }

  private void authenticateUser(String username, HttpServletRequest request) {
    var user = userDetailsService.loadUserByUsername(username);
    var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
    log.debug("Authenticated user: {}", username);
  }

  private static boolean isAuthHeaderValid(String header) {
    return header != null && header.startsWith(BEARER_PREFIX);
  }
}
