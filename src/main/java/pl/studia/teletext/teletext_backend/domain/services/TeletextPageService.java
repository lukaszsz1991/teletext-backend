package pl.studia.teletext.teletext_backend.domain.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.exceptions.PageNotFoundException;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;

@Service
@RequiredArgsConstructor
public class TeletextPageService {

  private final TeletextPageRepository teletextPageRepository;
  private final TeletextPageMapper mapper;

  public List<TeletextPageResponse> getAllPagesWithContent() {
    var pages = teletextPageRepository.findAllWithContent();
    return pages.stream()
      .map(mapper::toPageResponse)
      .toList();
  }

  public TeletextPageResponse getPageWithContent(Integer pageNumber) {
    var page = teletextPageRepository.findByPageNumberWithContent(pageNumber)
      .orElseThrow(() -> new PageNotFoundException("Page with number " + pageNumber + " not found"));
    return mapper.toPageResponse(page);
  }
}
