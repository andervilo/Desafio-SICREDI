package br.com.sicredi.ressources;

import br.com.sicredi.dto.AssociadoRequest;
import br.com.sicredi.models.Associado;
import br.com.sicredi.repositories.AssociadoRepository;
import br.com.sicredi.services.AssociadoService;
import br.com.sicredi.services.ValidarCpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AssociadosRessource.class)
@Import(AssociadoService.class)
public class AssociadosRessourceTest {

    private static final String ASSOCIADO_NOME = "Nome Associado Test";
    private static final String ASSOCIADO_NOME_UPDATE = "Nome Associado Test Updated";
    private static final String ASSOCIADO_CPF = "32243991047";
    private static final String ASSOCIADO_ID = "dfc117424f7c11ec81d30242ac130003";
    private static final String ASSOCIADO_ID_NOT_FOUND = "dfc117424f7c11ec81d30242ac130004";
    private static final String ASSOCIADO_URI ="/api/associados" ;
    private static final String APPLICATION_JSON_VALUE = "application/json";

    @MockBean
    private AssociadoRepository repository;

    @Autowired
    private WebTestClient client;

    @MockBean
    private ValidarCpf validarCpf;

    Associado associado = new Associado();
    Associado associadoUpdated = new Associado();

    AssociadoRequest associadoUpdate = new AssociadoRequest();
    AssociadoRequest associadoCreate = new AssociadoRequest();

    @BeforeEach
    void setUp() {
        associado.setId(ASSOCIADO_ID);
        associado.setNome(ASSOCIADO_NOME);
        associado.setCpf(ASSOCIADO_CPF);

        associadoUpdated.setId(ASSOCIADO_ID);
        associadoUpdated.setNome(ASSOCIADO_NOME_UPDATE);
        associadoUpdated.setCpf(ASSOCIADO_CPF);

        associadoCreate.setNome(ASSOCIADO_NOME);
        associadoCreate.setCpf(ASSOCIADO_CPF);

        associadoUpdate.setNome(ASSOCIADO_NOME_UPDATE);
        associadoUpdate.setCpf(ASSOCIADO_CPF);

    }

    @Test
    void creatreAssociadoTest(){
        Mockito.when(repository.save(Mockito.any(Associado.class))).thenReturn(Mono.just(associado));
        client.post()
                .uri(ASSOCIADO_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(associadoCreate))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(ASSOCIADO_ID)
                .jsonPath("$.nome").isEqualTo(ASSOCIADO_NOME)
                .jsonPath("$.cpf").isEqualTo(ASSOCIADO_CPF);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Associado.class));
    }

    @Test
    void showByIdAssociadoTest(){
        Mockito.when(repository.findById(ASSOCIADO_ID)).thenReturn(Mono.just(associado));
        client.get()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(ASSOCIADO_ID)
                .jsonPath("$.nome").isEqualTo(ASSOCIADO_NOME)
                .jsonPath("$.cpf").isEqualTo(ASSOCIADO_CPF);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.any(String.class));
    }

    @Test
    void showByIdAssociadoTest_shouldReturnNotFound(){
        Mockito.when(repository.findById(ASSOCIADO_ID_NOT_FOUND)).thenReturn(Mono.empty());
        client.get()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID_NOT_FOUND)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();

        Mockito.verify(repository, Mockito.times(1)).findById(ASSOCIADO_ID_NOT_FOUND);
    }

    @Test
    void findAllAssociadoTest(){
        Mockito.when(repository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(associado)));
        client.get()
                .uri(ASSOCIADO_URI)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].id").isEqualTo(ASSOCIADO_ID)
                .jsonPath("$[0].nome").isEqualTo(ASSOCIADO_NOME)
                .jsonPath("$[0].cpf").isEqualTo(ASSOCIADO_CPF);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void findAllAssociadoTest_shouldReturnVoidArray(){
        Mockito.when(repository.findAll()).thenReturn(Flux.empty());
        client.get()
                .uri(ASSOCIADO_URI)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void updateAssociadoTest(){
        Mockito.when(repository.findById(ASSOCIADO_ID)).thenReturn(Mono.just(associado));
        Mockito.when(repository.save(Mockito.any(Associado.class))).thenReturn(Mono.just(associadoUpdated));
        client.put()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(associadoUpdate))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(ASSOCIADO_ID)
                .jsonPath("$.nome").isEqualTo(ASSOCIADO_NOME_UPDATE)
                .jsonPath("$.cpf").isEqualTo(ASSOCIADO_CPF);

        Mockito.verify(repository, Mockito.times(1)).findById(ASSOCIADO_ID);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Associado.class));
    }

    @Test
    void updateAssociadoTest_shouldReturnNotFound(){
        Mockito.when(repository.findById(Mockito.any(String.class))).thenReturn(Mono.empty());
        Mockito.when(repository.save(Mockito.any(Associado.class))).thenReturn(Mono.just(associadoUpdated));
        client.put()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID_NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(associadoUpdate))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();

        Mockito.verify(repository, Mockito.times(1)).findById(ASSOCIADO_ID_NOT_FOUND);
    }

    @Test
    void deleteAssociadoTest(){
        Mockito.when(repository.findById(ASSOCIADO_ID)).thenReturn(Mono.just(associado));
        Mockito.when(repository.delete(Mockito.any(Associado.class))).thenReturn(Mono.empty());
        client.delete()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful();

        Mockito.verify(repository, Mockito.times(1)).findById(ASSOCIADO_ID);
        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.any(Associado.class));
    }

    @Test
    void deleteAssociadoTest_shouldReturnNotFound(){
        Mockito.when(repository.findById(ASSOCIADO_ID_NOT_FOUND)).thenReturn(Mono.empty());
        client.delete()
                .uri(ASSOCIADO_URI+"/"+ASSOCIADO_ID_NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isNotFound();

        Mockito.verify(repository, Mockito.times(1)).findById(ASSOCIADO_ID_NOT_FOUND);
    }
}
