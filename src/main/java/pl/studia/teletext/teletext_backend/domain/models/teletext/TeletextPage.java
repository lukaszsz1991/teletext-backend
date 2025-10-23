package pl.studia.teletext.teletext_backend.domain.models.teletext;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "pages")
@Data
public class TeletextPage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private Integer pageNumber;

  @Column(nullable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextCategory category;

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeletextPageContent> content;

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeletextPageStats> stats;

  @CreationTimestamp
  private Timestamp createdAt;

  @UpdateTimestamp
  private Timestamp updatedAt;

  private Timestamp deletedAt;
}
