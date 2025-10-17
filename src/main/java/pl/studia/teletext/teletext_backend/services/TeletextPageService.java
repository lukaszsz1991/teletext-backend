package pl.studia.teletext.teletext_backend.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.dtos.TeletextPageResponse;
import pl.studia.teletext.teletext_backend.mappers.TeletextPageMapper;
import pl.studia.teletext.teletext_backend.models.TeletextPage;
import pl.studia.teletext.teletext_backend.repositories.TeletextPageRepository;

@Service
@RequiredArgsConstructor
public class TeletextPageService {

  private final TeletextPageRepository teletextPageRepository;
  private final TeletextPageMapper mapper;

  public List<TeletextPageResponse> getAllPagesWithContent() {
    List<TeletextPage> pages = teletextPageRepository.findAllWithContent();
    return pages.stream()
      .map(mapper::toPageResponse)
      .toList();
  }
}
