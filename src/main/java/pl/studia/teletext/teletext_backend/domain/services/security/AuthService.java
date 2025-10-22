package pl.studia.teletext.teletext_backend.domain.services.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.admin.dtos.LoginRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.LoginResponse;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  public LoginResponse authenticate(LoginRequest request) {
    log.debug("Authenticating user: {}", request.username());
    var authToken = new UsernamePasswordAuthenticationToken(
      request.username(),
      request.password()
    );
    authenticationManager.authenticate(authToken);
    var userDetails = userDetailsService.loadUserByUsername(request.username());
    var jwtToken = jwtService.generateToken(userDetails);
    return new LoginResponse(jwtToken, "Bearer", jwtService.getExpirationMs());
  }
}
