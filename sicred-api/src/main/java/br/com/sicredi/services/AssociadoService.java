package br.com.sicredi.services;

import br.com.sicredi.dto.AssociadoRequest;
import br.com.sicredi.dto.AssociadoResponse;
import br.com.sicredi.models.Associado;
import br.com.sicredi.repositories.AssociadoRepository;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Service
public class AssociadoService {

    private AssociadoRepository associadoRepository;

    private ValidarCpf validarCpf;

    public AssociadoService(AssociadoRepository associadoRepository, ValidarCpf validarCpf) {
        this.associadoRepository = associadoRepository;
        this.validarCpf = validarCpf;
    }

    public Mono<AssociadoResponse> create(AssociadoRequest associadoRequest){
        validarCpf.validarCpf(associadoRequest.getCpf());
        var toSave = associadoRequest.creareTo();
        Mono<Associado> associadoMono = null;
        try{
            associadoMono = associadoRepository.save(toSave);
        } catch (MongoWriteException e){
            e.printStackTrace();
        }

        return associadoMono.map(AssociadoResponse::createFrom);
    }

    public Mono<ResponseEntity<AssociadoResponse>> showById(String id){
        return  associadoRepository.findById(id)
                .map(existingAssociado -> ResponseEntity.ok(AssociadoResponse.createFrom(existingAssociado)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Flux<AssociadoResponse> getAll(){
        return associadoRepository.findAll().map(AssociadoResponse::createFrom);
    }

    public Mono<ResponseEntity<AssociadoResponse>> update(AssociadoRequest associadoRequest, String id){
        return associadoRepository.findById(id)
                .flatMap(existingAssociado -> {
                    existingAssociado.setId(id);
                    existingAssociado.setNome(associadoRequest.getNome());
                    existingAssociado.setCpf(associadoRequest.getCpf());
                    return associadoRepository.save(existingAssociado);
                })
                .map(updatedAssociado -> ResponseEntity.ok(AssociadoResponse.createFrom(updatedAssociado)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<Void>> deleteById(String id){
        return associadoRepository.findById(id)
                .flatMap(existingAssociado ->
                        associadoRepository.delete(existingAssociado)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())))
                        .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
