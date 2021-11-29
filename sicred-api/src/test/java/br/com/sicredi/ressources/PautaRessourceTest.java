package br.com.sicredi.ressources;

import br.com.sicredi.dto.PautaIniciarVotacaoRequest;
import br.com.sicredi.dto.PautaRequest;
import br.com.sicredi.dto.PautaVotoRequest;
import br.com.sicredi.enums.StatusVotacao;
import br.com.sicredi.enums.TipoPeriodoVotacao;
import br.com.sicredi.enums.TipoVoto;
import br.com.sicredi.events.KafkaPautaCreateEvent;
import br.com.sicredi.models.Associado;
import br.com.sicredi.models.Pauta;
import br.com.sicredi.repositories.AssociadoRepository;
import br.com.sicredi.repositories.PautaRepository;
import br.com.sicredi.services.PautaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PautaRessource.class)
@Import({PautaService.class})
class PautaRessourceTest {

    private static final String PAUTA_DESCRICAO = "Descrição Pauta Test";
    private static final String PAUTA_URI ="/api/pautas" ;
    private static final String APPLICATION_JSON_VALUE = "application/json";
    private static final String PAUTA_ID = "61a4077f780db51ab4250241";
    private static final String ASSOCIADO_NOME = "Nome Associado Test";
    private static final String ASSOCIADO_CPF = "32243991047";
    private static final String ASSOCIADO_ID = "dfc117424f7c11ec81d30242ac130003";

    private static final String ASSOCIADO_NOME_JA_VOTOU = "Nome Associado Test2";
    private static final String ASSOCIADO_CPF_JA_VOTOU = "32243991049";
    private static final String ASSOCIADO_ID_JA_VOTOU = "dfc117424f7c11ec81d30242ac130004";

    @MockBean
    private PautaRepository pautaRepository;

    @MockBean
    private AssociadoRepository associadoRepository;

    @MockBean
    private KafkaPautaCreateEvent kafkaPautaCreateEvent;

    @Autowired
    private WebTestClient client;

    private PautaRequest pautaRequest = new PautaRequest();

    private Pauta pauta = new Pauta();

    private Pauta pautaEmAndamento = new Pauta();

    private Pauta pautaFinalizada = new Pauta();

    private PautaIniciarVotacaoRequest iniciarVotacaoRequest = new PautaIniciarVotacaoRequest();

    private PautaVotoRequest votoRequest = new PautaVotoRequest();

    private Associado associado = new Associado();

    private Associado associadoJavotou = new Associado();

    @BeforeEach
    void setUp() {
        pautaRequest.setDescricaoPauta(PAUTA_DESCRICAO);

        pauta.setId(PAUTA_ID);
        pauta.setDescricaoPauta(PAUTA_DESCRICAO);
        pauta.setStatusVotacao(StatusVotacao.NAO_INICIADA);
        pauta.setDataHoraAbeturaVotacao(null);
        pauta.setDataHoraFechamentoVotacao(null);

        pautaEmAndamento.setId(PAUTA_ID);
        pautaEmAndamento.setDescricaoPauta(PAUTA_DESCRICAO);
        pautaEmAndamento.setStatusVotacao(StatusVotacao.EM_ANDAMENTO);
        pautaEmAndamento.setDataHoraAbeturaVotacao(LocalDateTime.now());
        pautaEmAndamento.setDataHoraFechamentoVotacao(LocalDateTime.now().plusMinutes(2));

        pautaFinalizada.setId(PAUTA_ID);
        pautaFinalizada.setDescricaoPauta(PAUTA_DESCRICAO);
        pautaFinalizada.setStatusVotacao(StatusVotacao.FINALIZADA);
        pautaFinalizada.setDataHoraAbeturaVotacao(LocalDateTime.now());
        pautaFinalizada.setDataHoraFechamentoVotacao(LocalDateTime.now().plusMinutes(2));

        iniciarVotacaoRequest.setPautaId(PAUTA_ID);
        iniciarVotacaoRequest.setTipoPeriodoVotacao(TipoPeriodoVotacao.MINUTO);
        iniciarVotacaoRequest.setDuracao(1);

        associado.setId(ASSOCIADO_ID);
        associado.setNome(ASSOCIADO_NOME);
        associado.setCpf(ASSOCIADO_CPF);

        associadoJavotou.setId(ASSOCIADO_ID_JA_VOTOU);
        associadoJavotou.setNome(ASSOCIADO_NOME_JA_VOTOU);
        associadoJavotou.setCpf(ASSOCIADO_CPF_JA_VOTOU);

        pautaEmAndamento.addVotante(associadoJavotou);

        votoRequest.setPauta_id(pautaEmAndamento.getId());
        votoRequest.setAssociado_id(associado.getId());
        votoRequest.setVoto(TipoVoto.SIM);
    }

    @Test
    void createPautaTest() {
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pauta));
        client.post()
                .uri(PAUTA_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(pautaRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(PAUTA_ID)
                .jsonPath("$.descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$.dataHoraAbeturaVotacao").isEqualTo(null)
                .jsonPath("$.dataHoraFechamentoVotacao").isEqualTo(null)
                .jsonPath("$.totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$.totalVotosContrarios").isEqualTo(0)
                .jsonPath("$.statusVotacao").isEqualTo(StatusVotacao.NAO_INICIADA.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).save(Mockito.any(Pauta.class));
    }

    @Test
    void findAllTest() {
        Mockito.when(pautaRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(pauta)));
        client.get()
                .uri(PAUTA_URI)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(PAUTA_ID)
                .jsonPath("$[0].descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$[0].dataHoraAbeturaVotacao").isEqualTo(null)
                .jsonPath("$[0].dataHoraFechamentoVotacao").isEqualTo(null)
                .jsonPath("$[0].totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$[0].totalVotosContrarios").isEqualTo(0)
                .jsonPath("$[0].statusVotacao").isEqualTo(StatusVotacao.NAO_INICIADA.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findAll();
    }

    @Test
    void showById() {
        Mockito.when(pautaRepository.findById(PAUTA_ID)).thenReturn(Mono.just(pauta));
        client.get()
                .uri(PAUTA_URI+"/"+PAUTA_ID)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(PAUTA_ID)
                .jsonPath("$.descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$.dataHoraAbeturaVotacao").isEqualTo(null)
                .jsonPath("$.dataHoraFechamentoVotacao").isEqualTo(null)
                .jsonPath("$.totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$.totalVotosContrarios").isEqualTo(0)
                .jsonPath("$.statusVotacao").isEqualTo(StatusVotacao.NAO_INICIADA.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
    }

    @Test
    void findAllByStatus_NAO_INICIADA(){
        Mockito.when(pautaRepository.findByStatusVotacao(StatusVotacao.NAO_INICIADA)).thenReturn(Flux.fromIterable(Arrays.asList(pauta)));
        client.get()
                .uri(PAUTA_URI+"/status/"+StatusVotacao.NAO_INICIADA.name())
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(PAUTA_ID)
                .jsonPath("$[0].descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$[0].dataHoraAbeturaVotacao").isEqualTo(null)
                .jsonPath("$[0].dataHoraFechamentoVotacao").isEqualTo(null)
                .jsonPath("$[0].totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$[0].totalVotosContrarios").isEqualTo(0)
                .jsonPath("$[0].statusVotacao").isEqualTo(StatusVotacao.NAO_INICIADA.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findByStatusVotacao(Mockito.any(StatusVotacao.class));
    }

    @Test
    void findAllByStatus_EM_ANDAMENTO(){
        Mockito.when(pautaRepository.findByStatusVotacao(StatusVotacao.EM_ANDAMENTO)).thenReturn(Flux.fromIterable(Arrays.asList(pautaEmAndamento)));
        client.get()
                .uri(PAUTA_URI+"/status/"+StatusVotacao.EM_ANDAMENTO.name())
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(PAUTA_ID)
                .jsonPath("$[0].descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$[0].dataHoraAbeturaVotacao").isNotEmpty()
                .jsonPath("$[0].dataHoraFechamentoVotacao").isNotEmpty()
                .jsonPath("$[0].totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$[0].totalVotosContrarios").isEqualTo(0)
                .jsonPath("$[0].statusVotacao").isEqualTo(StatusVotacao.EM_ANDAMENTO.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findByStatusVotacao(Mockito.any(StatusVotacao.class));
    }

    @Test
    void findAllByStatus_FINALIZADA(){
        Mockito.when(pautaRepository.findByStatusVotacao(StatusVotacao.FINALIZADA)).thenReturn(Flux.fromIterable(Arrays.asList(pautaFinalizada)));
        client.get()
                .uri(PAUTA_URI+"/status/"+StatusVotacao.FINALIZADA.name())
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(PAUTA_ID)
                .jsonPath("$[0].descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$[0].dataHoraAbeturaVotacao").isNotEmpty()
                .jsonPath("$[0].dataHoraFechamentoVotacao").isNotEmpty()
                .jsonPath("$[0].totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$[0].totalVotosContrarios").isEqualTo(0)
                .jsonPath("$[0].statusVotacao").isEqualTo(StatusVotacao.FINALIZADA.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findByStatusVotacao(Mockito.any(StatusVotacao.class));
    }

    @Test
    void inciarVotacao_PautaNaoIniciada() {
        Mockito.when(pautaRepository.findById(iniciarVotacaoRequest.getPautaId())).thenReturn(Mono.just(pauta));
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pautaEmAndamento));
        client.post()
                .uri(PAUTA_URI+"/iniciar-votacao")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(iniciarVotacaoRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(PAUTA_ID)
                .jsonPath("$.descricaoPauta").isEqualTo(PAUTA_DESCRICAO)
                .jsonPath("$.dataHoraAbeturaVotacao").isNotEmpty()
                .jsonPath("$.dataHoraFechamentoVotacao").isNotEmpty()
                .jsonPath("$.totalVotosFavoraveis").isEqualTo(0)
                .jsonPath("$.totalVotosContrarios").isEqualTo(0)
                .jsonPath("$.statusVotacao").isEqualTo(StatusVotacao.EM_ANDAMENTO.name());

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
        Mockito.verify(pautaRepository, Mockito.times(1)).save(Mockito.any(Pauta.class));
    }

    @Test
    void inciarVotacao_PautaEmAndamento() {
        Mockito.when(pautaRepository.findById(iniciarVotacaoRequest.getPautaId())).thenReturn(Mono.just(pautaEmAndamento));
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pautaEmAndamento));
        client.post()
                .uri(PAUTA_URI+"/iniciar-votacao")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(iniciarVotacaoRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value());

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
    }

    @Test
    void inciarVotacao_PautaFinalizada() {
        Mockito.when(pautaRepository.findById(iniciarVotacaoRequest.getPautaId())).thenReturn(Mono.just(pautaFinalizada));
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pautaEmAndamento));
        client.post()
                .uri(PAUTA_URI+"/iniciar-votacao")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(iniciarVotacaoRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value());

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
    }

    @Test
    void votarPautaEmAndamento() {
        Mockito.when(associadoRepository.findById(associado.getId())).thenReturn(Mono.just(associado));
        Mockito.when(pautaRepository.findById(iniciarVotacaoRequest.getPautaId())).thenReturn(Mono.just(pautaEmAndamento));
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pautaEmAndamento));
        client.post()
                .uri(PAUTA_URI+"/votar")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(votoRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().is2xxSuccessful();

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
        Mockito.verify(pautaRepository, Mockito.times(1)).save(Mockito.any(Pauta.class));
    }

    @Test
    void votarPautaEmAndamento_associadoJaVotou() {
        Mockito.when(associadoRepository.findById(Mockito.any(String.class))).thenReturn(Mono.just(associadoJavotou));
        Mockito.when(pautaRepository.findById(iniciarVotacaoRequest.getPautaId())).thenReturn(Mono.just(pautaEmAndamento));
        Mockito.when(pautaRepository.save(Mockito.any(Pauta.class))).thenReturn(Mono.just(pautaEmAndamento));
        client.post()
                .uri(PAUTA_URI+"/votar")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(votoRequest))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value());;

        Mockito.verify(pautaRepository, Mockito.times(1)).findById(PAUTA_ID);
    }
}