package pl.studia.teletext.teletext_backend.clients.tvp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.LocalDateTime;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("APCData")
public record TvpResponse(
    @JacksonXmlElementWrapper(useWrapping = false) @JacksonXmlProperty(localName = "prrecord")
        PrRecord[] records) {
  public record PrRecord(
      @JacksonXmlProperty(localName = "REAL_DATE_TIME") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime realDateTime,
      @JacksonXmlProperty(localName = "PTITEL") String title,
      @JacksonXmlProperty(localName = "EPG") String description) {}
}
