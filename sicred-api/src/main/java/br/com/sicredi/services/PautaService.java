package br.com.sicredi.services;

import br.com.sicredi.dto.PautaIniciarVotacaoRequest;
import br.com.sicredi.dto.PautaRequest;
import br.com.sicredi.dto.PautaResponse;
import br.com.sicredi.dto.PautaVotoRequest;
import br.com.sicredi.enums.StatusVotacao;
import br.com.sicredi.events.KafkaPautaCreateEvent;
import br.com.sicredi.models.Associado;
import br.com.sicredi.models.Pauta;
import br.com.sicredi.repositories.AssociadoRepository;
import br.com.sicredi.repositories.PautaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PautaService {

    private PautaRepository pautaRepository;

    private AssociadoRepository associadoRepository;

    private KafkaPautaCreateEvent kafkaPautaCreateEvent;

    public PautaService(PautaRepository pautaRepository, AssociadoRepository associadoRepository, KafkaPautaCreateEvent kafkaPautaCreateEvent) {
        this.pautaRepository = pautaRepository;
        this.associadoRepository = associadoRepository;
        this.kafkaPautaCreateEvent = kafkaPautaCreateEvent;
    }

    public Mono<PautaResponse> create(PautaRequest pautaRequest){
        return pautaRepository.save(new Pauta(pautaRequest.getDescricaoPauta()))
                .map(PautaResponse::createFrom);
    }

    public Flux<PautaResponse> getAll() {
        return pautaRepository.findAll()
                .map(PautaResponse::createFrom);
    }

    public Flux<PautaResponse> getAllByStatus(StatusVotacao statusVotacao) {
        return pautaRepository.findByStatusVotacao(statusVotacao)
                .map(PautaResponse::createFrom);
    }

    public Mono<PautaResponse> iniciarVotacao(PautaIniciarVotacaoRequest iniciarVotacaoRequest) {
        return pautaRepository.findById(iniciarVotacaoRequest.getPautaId())
                .flatMap(existingPauta ->{
                    if(!existingPauta.getStatusVotacao().equals(StatusVotacao.NAO_INICIADA)){
                        if(existingPauta.getStatusVotacao().equals(StatusVotacao.EM_ANDAMENTO))
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Votação já iniciada para essa Pauta!"));

                        if(existingPauta.getStatusVotacao().equals(StatusVotacao.FINALIZADA))
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Votação já finalizada para essa Pauta!"));
                        //return badRequest( "Votação não iniciada para essa Pauta!");
                    }
                    existingPauta.setStatusVotacao(StatusVotacao.EM_ANDAMENTO);
                    existingPauta.setDataHoraAbeturaVotacao(LocalDateTime.now());

                    switch (iniciarVotacaoRequest.getTipoPeriodoVotacao()){
                        case DIA: existingPauta.setDataHoraFechamentoVotacao(existingPauta.getDataHoraAbeturaVotacao().plusDays(iniciarVotacaoRequest.getDuracao())); break;
                        case HORA: existingPauta.setDataHoraFechamentoVotacao(existingPauta.getDataHoraAbeturaVotacao().plusHours(iniciarVotacaoRequest.getDuracao())); break;
                        case MINUTO: existingPauta.setDataHoraFechamentoVotacao(existingPauta.getDataHoraAbeturaVotacao().plusMinutes(iniciarVotacaoRequest.getDuracao())); break;
                    }
                    return pautaRepository.save(existingPauta);
                })
                .map(PautaResponse::createFrom);
    }

    public Mono<ResponseEntity<PautaResponse>> showById(String pautaId) {
        return pautaRepository.findById(pautaId)
                .map(existingPauta -> ResponseEntity.ok(PautaResponse.createFrom(existingPauta)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public void fecharVotacoesDePautas(){
        LocalDateTime agora = LocalDateTime.now();
        pautaRepository.findByStatusVotacao(StatusVotacao.EM_ANDAMENTO)
                .flatMap(pauta ->{
                    if(pauta.getDataHoraFechamentoVotacao().isBefore(agora)){
                        pauta.setStatusVotacao(StatusVotacao.FINALIZADA);

                        try {
                            kafkaPautaCreateEvent.publicEvent(pauta);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        return pautaRepository.save(pauta);
                    }
                    return Mono.just(pauta);
                }).subscribe();
    }

    public Mono<ResponseEntity<Void>> votar(PautaVotoRequest pautaVotoRequest) {
        LocalDateTime agora = LocalDateTime.now();
        return pautaRepository.findById(pautaVotoRequest.getPauta_id())
                .flatMap(pauta -> {
                    if(pauta.getStatusVotacao().equals(StatusVotacao.NAO_INICIADA)){
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Votação não iniciada para essa Pauta!"));
                    }

                    if(pauta.getDataHoraFechamentoVotacao().isBefore(agora)){
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Votação já encerrada para essa Pauta!"));
                    }

                    return associadoRepository
                            .findById(pautaVotoRequest.getAssociado_id())
                            .flatMap(associado -> {
                                if (pauta.getListaVotantes().contains(associado)) {
                                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Associado já votou nesta Pauta!"));
                                }
                                pauta.addVotante(associado);

                                switch (pautaVotoRequest.getVoto()){
                                    case SIM: pauta.addVotoFavoravel(); break;
                                    case NAO: pauta.addVotoContrario(); break;
                                }
                                return pautaRepository.save(pauta);
                            })
                            .then(Mono.just(ResponseEntity.ok().<Void>build()))
                            .defaultIfEmpty(ResponseEntity.notFound().build());
                })
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
