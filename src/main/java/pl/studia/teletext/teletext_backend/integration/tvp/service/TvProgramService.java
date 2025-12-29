package pl.studia.teletext.teletext_backend.integration.tvp.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.tvp.client.TvpClient;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvProgramResponse;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpChannel;
import pl.studia.teletext.teletext_backend.integration.tvp.mapper.TvProgramMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TvProgramService {

  private final TvpClient tvpClient;
  private final TvProgramMapper tvProgramMapper;

  public Mono<TvProgramResponse> getTvProgram(TvpChannel channel, LocalDate date) {
    return tvpClient
        .fetchTvProgram(channel, date)
        .map(program -> tvProgramMapper.toResponse(channel.getDisplayName(), date, program));
  }
}
