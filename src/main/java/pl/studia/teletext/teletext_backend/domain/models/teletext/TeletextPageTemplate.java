package pl.studia.teletext.teletext_backend.domain.models.teletext;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "page_templates")
@Data
public class TeletextPageTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String source; // must match the TeletextCategory source

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextCategory category;

  private String configJson;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private Timestamp deletedAt;
}
