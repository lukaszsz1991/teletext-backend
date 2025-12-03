package pl.studia.teletext.teletext_backend.domain.services.pages;

import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextPage;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.exceptions.PageNotFoundException;

@Service
@RequiredArgsConstructor
public class TeletextPageService {

  private final TeletextPageGeneratorService pageGeneratorService;
  private final TeletextPageRepository teletextPageRepository;
  private final TeletextPageMapper mapper;

  public TeletextDetailedPageResponse getPageWithContent(Integer pageNumber) {
    var page =
        teletextPageRepository
            .findByPageNumberWithContent(pageNumber)
            .orElseThrow(
                () -> new PageNotFoundException("Nie odnaleziono strony o numerze " + pageNumber));

    var result =
        page.getTemplate() != null
            ? pageGeneratorService.generatePageFromTemplate(page).block()
            : page;

    return mapper.toDetailedPageResponse(result);
  }

  public List<TeletextPageResponse> getPagesByCategory(TeletextCategory category, String title) {
    String t = (title == null) ? "" : title.toLowerCase().trim();
    return teletextPageRepository.findByCategoryWithContent(category).stream()
        .filter(
            page -> {
              if (t.isBlank()) return true;
              var contentTitle =
                  page.getContent() != null ? page.getContent().getTitle().toLowerCase() : "";
              var templateTitle =
                  page.getTemplate() != null ? page.getTemplate().getName().toLowerCase() : "";
              return contentTitle.contains(t) || templateTitle.contains(t);
            })
        .map(mapper::toPageResponse)
        .sorted(Comparator.comparing(TeletextPageResponse::pageNumber))
        .toList();
  }

  public List<TeletextPageResponse> getAllPages(TeletextCategory category) {
    return Optional.ofNullable(category)
        .map(teletextPageRepository::findAllByCategory)
        .orElseGet(teletextPageRepository::findAllActive)
        .stream()
        .map(mapper::toPageResponse)
        .toList();
  }

  // TODO: should use CreateTeletextPageRequest dto instead of entity
  @Transactional
  public TeletextPage save(TeletextPage page) {
    return teletextPageRepository.save(page);
  }
}
