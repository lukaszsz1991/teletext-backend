package pl.studia.teletext.teletext_backend.domain.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.studia.teletext.teletext_backend.domain.models.security.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
