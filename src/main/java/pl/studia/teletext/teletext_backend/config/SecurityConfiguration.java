package pl.studia.teletext.teletext_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.studia.teletext.teletext_backend.config.middleware.JwtAuthFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(request -> request
        .requestMatchers("/api/public/**").permitAll()
        .requestMatchers("/api/admin/auth/login").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().denyAll()
      )
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new Argon2PasswordEncoder(16,16,1,65536,4);
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    var authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return new ProviderManager(authProvider);
  }
}
