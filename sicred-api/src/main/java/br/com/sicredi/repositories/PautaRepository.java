package br.com.sicredi.repositories;

import br.com.sicredi.enums.StatusVotacao;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.sicredi.models.Pauta;
import reactor.core.publisher.Flux;

public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {

    Flux<Pauta> findByStatusVotacao(StatusVotacao statusVotacao);
}