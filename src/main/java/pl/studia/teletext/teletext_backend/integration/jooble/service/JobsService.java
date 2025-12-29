package pl.studia.teletext.teletext_backend.integration.jooble.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.jooble.client.JoobleClient;
import pl.studia.teletext.teletext_backend.integration.jooble.domain.JobResponse;
import pl.studia.teletext.teletext_backend.integration.jooble.domain.JoobleRequest;
import pl.studia.teletext.teletext_backend.integration.jooble.mapper.JobsMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JobsService {

  private final JoobleClient joobleClient;
  private final JobsMapper jobsMapper;

  public Flux<JobResponse> getAllJobs(@Valid JoobleRequest request) {
    return joobleClient
        .getJobs(request)
        .flatMapMany(
            resp ->
                Flux.fromIterable(resp.jobs())
                    .map(
                        job ->
                            jobsMapper.toResponse(
                                job, resp.totalCount(), request.keywords(), request.location())))
        .sort((a, b) -> b.updatedAt().compareTo(a.updatedAt()));
  }

  public Mono<JobResponse> getJobByAddingOrder(@Valid JoobleRequest request, int addedOrder) {
    return getAllJobs(request)
        .elementAt(addedOrder - 1)
        .onErrorResume(IndexOutOfBoundsException.class, ex -> Mono.empty());
  }
}
