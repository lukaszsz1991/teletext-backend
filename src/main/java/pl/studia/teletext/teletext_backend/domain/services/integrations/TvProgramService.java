package pl.studia.teletext.teletext_backend.domain.services.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TvProgramResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.TvProgramMapper;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpChannel;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

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
