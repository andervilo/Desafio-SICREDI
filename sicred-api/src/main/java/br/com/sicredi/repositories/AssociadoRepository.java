package br.com.sicredi.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import br.com.sicredi.models.Associado;

public interface AssociadoRepository extends ReactiveMongoRepository<Associado, String>{

}
