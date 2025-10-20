package pl.studia.teletext.teletext_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "page_stats")
@Data
@NoArgsConstructor
public class TeletextPageStats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "page_id", referencedColumnName = "id")
  private TeletextPage page;

  @Column(nullable = false)
  private Timestamp openedAt;

  public TeletextPageStats(TeletextPage page) {
    this.page = page;
    this.openedAt = new Timestamp(System.currentTimeMillis());
  }

  @PrePersist
  protected void onCreate() {
    this.openedAt = new Timestamp(System.currentTimeMillis());
  }
}
