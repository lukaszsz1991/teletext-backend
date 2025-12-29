package pl.studia.teletext.teletext_backend.auth.repository;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.studia.teletext.teletext_backend.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Query("SELECT rt FROM RefreshToken rt JOIN FETCH rt.user WHERE rt.token = :token")
  Optional<RefreshToken> findByTokenWithUser(String token);

  @Query("SELECT rt FROM RefreshToken rt WHERE rt.user.username = :username")
  Optional<RefreshToken> findByUsername(String username);

  @Modifying
  @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
  void deleteAllExpiredSince(Instant now);
}
