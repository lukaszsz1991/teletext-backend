package pl.studia.teletext.teletext_backend.config.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Value("${application.version}")
  private String appVersion;

  @Value("${spring.mail.username:teletext@example.com}")
  private String email;

  @Bean
  public OpenAPI teletextOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Teletext Backend API")
                .description("API documentation for the Teletext Backend application.")
                .version(appVersion)
                .contact(new Contact().name("Teletext Dev Team").email(email)))
        .components(
            new Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name("Authorization")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
