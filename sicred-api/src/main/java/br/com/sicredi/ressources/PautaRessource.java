package br.com.sicredi.ressources;

import br.com.sicredi.dto.PautaIniciarVotacaoRequest;
import br.com.sicredi.dto.PautaRequest;
import br.com.sicredi.dto.PautaResponse;
import br.com.sicredi.dto.PautaVotoRequest;
import br.com.sicredi.enums.StatusVotacao;
import br.com.sicredi.services.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/pautas")
@Tag(name = "Pautas", description = "API pautas")
public class PautaRessource {

    private PautaService pautaService;

    public PautaRessource(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Criar Pauta")
    @PostMapping
    public Mono<PautaResponse> create(@RequestBody PautaRequest pautaRequest){
        return pautaService.create(pautaRequest);
    }

    @Operation(summary = "Obter todas as Pautas")
    @GetMapping
    public Flux<PautaResponse> findAll(){
        return pautaService.getAll();
    }

    @Operation(summary = "Obter Pauta por ID")
    @GetMapping("/{pautaId}")
    public Mono<ResponseEntity<PautaResponse>> showById(@PathVariable String pautaId){
        return pautaService.showById(pautaId);
    }

    @Operation(summary = "Obter todas Pautas por Status Votação")
    @GetMapping("/status/{statusVotacao}")
    public Flux<PautaResponse> findAllByStatus(@PathVariable StatusVotacao statusVotacao){
        return pautaService.getAllByStatus(statusVotacao);
    }

    @Operation(summary = "Iniciar período de Votação de um Pauta")
    @PostMapping("/iniciar-votacao")
    public Mono<PautaResponse> inciarVotacao(@RequestBody PautaIniciarVotacaoRequest iniciarVotacaoRequest){
        return pautaService.iniciarVotacao(iniciarVotacaoRequest);
    }

    @Operation(summary = "Adicionar voto a uma Pauta")
    @PostMapping("/votar")
    public Mono<ResponseEntity<Void>> votarPauta(@RequestBody PautaVotoRequest pautaVotoRequest){
        return pautaService.votar(pautaVotoRequest);
    }

}
