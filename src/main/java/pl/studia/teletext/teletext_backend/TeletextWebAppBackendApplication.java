package pl.studia.teletext.teletext_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.studia.teletext.teletext_backend.config.properties.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TeletextWebAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeletextWebAppBackendApplication.class, args);
	}

}
