package pl.studia.teletext.teletext_backend.domain.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.admin.dtos.TeletextPageStatsResponse;
import pl.studia.teletext.teletext_backend.api.admin.mappers.TeletextPageStatsMapper;
import pl.studia.teletext.teletext_backend.exceptions.PageNotFoundException;
import pl.studia.teletext.teletext_backend.domain.models.TeletextPageStats;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageRepository;
import pl.studia.teletext.teletext_backend.domain.repositories.TeletextPageStatsRepository;

@Service
@RequiredArgsConstructor
public class TeletextPageStatsService {

  private final TeletextPageRepository pageRepository;
  private final TeletextPageStatsRepository statsRepository;
  private final TeletextPageStatsMapper mapper;

  @Transactional
  public void recordPageVisit(Long pageId) {
    var page = pageRepository.findById(pageId)
        .orElseThrow(() -> new PageNotFoundException("Page with id " + pageId + " not found"));
    var stat = new TeletextPageStats(page);
    statsRepository.save(stat);
  }

  public TeletextPageStatsResponse getStatsForPage(Integer pageNumber) {
    var stats = statsRepository.findAllByPageNumber(pageNumber);
    return mapper.toPageStatsResponse(stats);
  }
}
