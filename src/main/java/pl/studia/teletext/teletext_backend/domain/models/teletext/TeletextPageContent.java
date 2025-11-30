package pl.studia.teletext.teletext_backend.domain.models.teletext;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "page_contents")
@Data
public class TeletextPageContent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // TODO: change content to something more complex
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "page_id", nullable = false)
  private TeletextPage page;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private Timestamp deletedAt;
}
