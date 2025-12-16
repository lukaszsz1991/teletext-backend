package pl.studia.teletext.teletext_backend.domain.models.teletext;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.TeletextPageTemplate;
import pl.studia.teletext.teletext_backend.exceptions.IllegalPageNumberException;

@Entity
@Table(name = "pages")
@Data
public class TeletextPage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Integer pageNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TeletextCategory category;

  @OneToOne(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
  private TeletextPageContent content;

  @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TeletextPageStats> stats;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "template_id")
  private TeletextPageTemplate template;

  @CreationTimestamp private Timestamp createdAt;

  @UpdateTimestamp private Timestamp updatedAt;

  private Timestamp deletedAt;

  @PrePersist
  @PreUpdate
  public void validate() {
    validatePageNumberRange();
    validateCategory();
  }

  public String getTitle() {
    if (this.content != null) return this.content.getTitle();
    if (this.template != null) return this.template.getName();
    throw new IllegalStateException(
        "Brak tytułu strony o numerze " + this.pageNumber + ". Nieprawidłowy stan obiektu.");
  }

  public String getType() {
    if (this.template != null) return "TEMPLATE";
    return "MANUAL";
  }

  private void validatePageNumberRange() {
    int mainPage = this.category.getMainPage();
    if (this.pageNumber < mainPage + 1 || this.pageNumber > mainPage + 99) {
      throw new IllegalPageNumberException(
          "Numer strony "
              + this.pageNumber
              + " jest poza zakresem dla kategorii "
              + this.category.getTitle());
    }
  }

  private void validateCategory() {
    if (this.template != null) {
      if (!this.category.equals(this.template.getCategory())) {
        throw new IllegalStateException(
            "Szablon strony należy do innej kategorii niż sama strona. Strona: "
                + this.pageNumber
                + ", kategoria strony: "
                + this.category
                + ", kategoria szablonu: "
                + this.template.getCategory());
      }
    }
  }
}
