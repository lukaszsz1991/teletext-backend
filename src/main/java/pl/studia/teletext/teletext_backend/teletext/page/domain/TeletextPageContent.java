package pl.studia.teletext.teletext_backend.teletext.page.domain;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Map;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "page_contents")
@Data
public class TeletextPageContent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextSource source;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> additionalData;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_id", nullable = false)
  private TeletextPage page;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private Timestamp deletedAt;

  @PrePersist
  @PreUpdate
  private void validate() {
    this.page.getCategory().validateSource(this.source);
  }
}
