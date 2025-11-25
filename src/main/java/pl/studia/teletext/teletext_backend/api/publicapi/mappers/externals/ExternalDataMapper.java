package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;

public interface ExternalDataMapper<T> {

  ExternalDataResponse toExternalDataResponse(T source);
  default Map<String, Object> toAdditionalData(T source) {
    return new HashMap<>();
  }
}
