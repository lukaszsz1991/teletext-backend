package pl.studia.teletext.teletext_backend.integration.tvp.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import pl.studia.teletext.teletext_backend.integration.tvp.util.JaxbLocalDateTimeAdapter;

@Data
@XmlRootElement(name = "APCData")
@XmlAccessorType(XmlAccessType.FIELD)
public class TvpResponse {

  @XmlElement(name = "prrecord")
  private List<PrRecord> records;

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class PrRecord {

    @XmlElement(name = "REAL_DATE_TIME")
    @XmlJavaTypeAdapter(JaxbLocalDateTimeAdapter.class)
    private LocalDateTime realDateTime;

    @XmlElement(name = "PTITEL")
    private String title;

    @XmlElement(name = "EPG")
    private String description;
  }
}
