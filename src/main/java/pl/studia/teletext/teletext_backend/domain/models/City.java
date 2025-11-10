package pl.studia.teletext.teletext_backend.domain.models;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record City(
  @NotBlank(message = "City name must not be blank")
  @Size(min = 1, max = 100, message = "City name must be between {min} and {max} chars long")
  String name,
  @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
  @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
  double latitude,
  @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
  @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
  double longitude
) { }
