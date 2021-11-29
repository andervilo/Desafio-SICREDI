package br.com.sicredi.ressources;

import br.com.sicredi.dto.AssociadoRequest;
import br.com.sicredi.dto.AssociadoResponse;
import br.com.sicredi.services.AssociadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/associados")
@Tag(name = "Associados", description = "API associados")
public class AssociadosRessource {

    private AssociadoService associadoService;

    public AssociadosRessource(AssociadoService associadoService) {
        this.associadoService = associadoService;
    }

    @Operation(summary = "Obter Associado por ID")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AssociadoResponse>> showById(@PathVariable String id){
        return associadoService.showById(id);
    }

    @Operation(summary = "Criar novo Associado")
    @PostMapping
    public Mono<AssociadoResponse> create(@RequestBody AssociadoRequest associadoRequest){
        return associadoService.create(associadoRequest);
    }

    @Operation(summary = "Obter todos as Associados")
    @GetMapping
    public Flux<AssociadoResponse> findAll(){
        return associadoService.getAll();
    }

    @Operation(summary = "Atualizar um Associado")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AssociadoResponse>> update(@RequestBody AssociadoRequest associadoRequest, @PathVariable String id){
        return associadoService.update(associadoRequest, id);
    }

    @Operation(summary = "Excluir um Associado")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> update(@PathVariable String id){
        return associadoService.deleteById(id);
    }
}
