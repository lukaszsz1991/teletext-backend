package pl.studia.teletext.teletext_backend.domain.services.integrations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.JobResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.JobsMapper;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleClient;
import pl.studia.teletext.teletext_backend.clients.jooble.JoobleRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JobsService {

  private final JoobleClient joobleClient;
  private final JobsMapper jobsMapper;

  public Flux<JobResponse> getAllJobs(@Valid JoobleRequest request) {
    return joobleClient.getJobs(request)
      .flatMapMany(resp -> Flux.fromIterable(resp.jobs())
        .map(job -> jobsMapper.toResponse(job, resp.totalCount(), request.keywords(), request.location())))
      .sort((a, b) -> b.updatedAt().compareTo(a.updatedAt()));
  }

  public Mono<JobResponse> getJobByAddingOrder(@Valid JoobleRequest request, int addedOrder) {
    return getAllJobs(request)
      .elementAt(addedOrder - 1)
      .onErrorResume(IndexOutOfBoundsException.class, ex -> Mono.empty());
  }
}
