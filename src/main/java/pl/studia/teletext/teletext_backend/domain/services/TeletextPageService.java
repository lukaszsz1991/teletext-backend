package pl.studia.teletext.teletext_backend.domain.services;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextDetailedPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.page.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextCategory;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.exceptions.PageNotFoundException;

@Service
@RequiredArgsConstructor
public class TeletextPageService {

  private final TeletextPageRepository teletextPageRepository;
  private final TeletextPageMapper mapper;

  public TeletextDetailedPageResponse getPageWithContent(Integer pageNumber) {
    var page =
        teletextPageRepository
            .findByPageNumberWithContent(pageNumber)
            .orElseThrow(
                () -> new PageNotFoundException("Page with number " + pageNumber + " not found"));
    return mapper.toDetailedPageResponse(page);
  }

  public List<TeletextPageResponse> getPagesByCategory(TeletextCategory category) {
    return teletextPageRepository.findByCategoryWithContent(category).stream()
        .map(mapper::toPageResponse)
        .sorted(Comparator.comparing(TeletextPageResponse::pageNumber))
        .toList();
  }
}
