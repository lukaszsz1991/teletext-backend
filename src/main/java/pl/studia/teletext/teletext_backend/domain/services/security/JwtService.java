package pl.studia.teletext.teletext_backend.domain.services.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.config.properties.JwtProperties;
import pl.studia.teletext.teletext_backend.exceptions.JwtValidatingException;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtService {

  private final JwtProperties jwtProperties;

  public String generateToken(UserDetails userDetails) {
    return buildToken(userDetails, jwtProperties.expirationMs());
  }

  public void validateToken(String token) {
    try {
      Claims claims = extractAllClaims(token);
      if (!jwtProperties.issuer().equals(claims.getIssuer())) {
        throw new JwtValidatingException("Nieprawidłowy wydawca JWT");
      }
      if (!claims.getAudience().contains(jwtProperties.audience())) {
        throw new JwtValidatingException("Nieprawidłowy odbiorca JWT");
      }
    } catch (Exception e) {
      log.error("Invalid JWT: {}", e.getMessage());
      throw new JwtValidatingException("Walidacja JWT nie powiodła się", e);
    }
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject)
        .orElseThrow(
            () -> new JwtValidatingException("Nie udało się wyodrębnić nazwy użytkownika z JWT"));
  }

  public Long getExpirationMs() {
    return jwtProperties.expirationMs();
  }

  private String buildToken(UserDetails userDetails, long expiration) {
    log.debug("Building new jwt token for user: {}", userDetails.getUsername());
    return Jwts.builder()
        .issuer(jwtProperties.issuer())
        .audience()
        .add(jwtProperties.audience())
        .and()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey())
        .compact();
  }

  private Claims extractAllClaims(String token) throws ExpiredJwtException {
    log.debug("Extracting all claims from token and verifying signature");
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException ex) {
      throw new JwtValidatingException("Token JWT wygasł");
    }
  }

  private <T> Optional<T> extractClaim(String token, Function<Claims, T> claim) {
    final Claims claims = extractAllClaims(token);
    return Optional.of(claim.apply(claims));
  }

  private SecretKey getSigningKey() throws DecodingException {
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
