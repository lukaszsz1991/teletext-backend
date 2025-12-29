package pl.studia.teletext.teletext_backend.integration.weather.domain;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record City(
    @NotBlank(message = "Nazwa miasta nie może być pusta")
        @Size(min = 1, max = 100, message = "Nazwa miasta musi mieć od 1 do 100 znaków")
        String name,
    @DecimalMin(value = "-90.0", message = "Szerokość geograficzna musi być >= {value}")
        @DecimalMax(value = "90.0", message = "Szerokość geograficzna musi być <= {value}")
        double latitude,
    @DecimalMin(value = "-180.0", message = "Długość geograficzna musi być >= {value}")
        @DecimalMax(value = "180.0", message = "Długość geograficzna musi być <= {value}")
        double longitude) {}
