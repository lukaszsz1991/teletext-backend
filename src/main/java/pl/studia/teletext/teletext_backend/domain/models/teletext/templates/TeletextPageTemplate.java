package pl.studia.teletext.teletext_backend.domain.models.teletext.templates;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

@Entity
@Table(name = "page_templates")
@Data
public class TeletextPageTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextSource source; // must match the TeletextCategory source

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextCategory category;

  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> configJson;

  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "template",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<TeletextPage> pages;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private Timestamp deletedAt;

  @PreUpdate
  @PrePersist
  private void validate() {
    this.category.validateSource(this.source);
  }
}
