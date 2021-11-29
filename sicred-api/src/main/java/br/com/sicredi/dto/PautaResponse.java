package br.com.sicredi.dto;

import br.com.sicredi.converter.LocalDateTimeDeserializer;
import br.com.sicredi.converter.LocalDateTimeSerializer;
import br.com.sicredi.models.Associado;
import br.com.sicredi.enums.StatusVotacao;
import br.com.sicredi.models.Pauta;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PautaResponse {

    @Schema(description = "ID da Pauta", example = "61a42665c834c356bd4c3828")
    private String id;

    @Schema(description = "Descrição da Pauta", example = "Exemplo de Descrição da Pauta")
    private String descricaoPauta;

    @Schema(description = "Data e hora da abertura do período de votação", example = "2021-11-28T22:01:45.024665800")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dataHoraAbeturaVotacao;

    @Schema(description = "Data e hora do fechamento do período de votação", example = "2021-11-28T22:01:45.024665800")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dataHoraFechamentoVotacao;

    @Schema(description = "Total de votos favoráveis", example = "15")
    private Long totalVotosFavoraveis;

    @Schema(description = "Total de votos contrários", example = "15")
    private Long totalVotosContrarios;

    @Schema(description = "Status da votação da Pauta", example = "NAO_INICIADA")
    private StatusVotacao statusVotacao;

    public PautaResponse(String id, String descricaoPauta, LocalDateTime dataHoraAbeturaVotacao, LocalDateTime dataHoraFechamentoVotacao, Long totalVotosFavoraveis, Long totalVotosContrarios, StatusVotacao statusVotacao) {
        this.id = id;
        this.descricaoPauta = descricaoPauta;
        this.dataHoraAbeturaVotacao = dataHoraAbeturaVotacao;
        this.dataHoraFechamentoVotacao = dataHoraFechamentoVotacao;
        this.totalVotosFavoraveis = totalVotosFavoraveis;
        this.totalVotosContrarios = totalVotosContrarios;
        this.statusVotacao = statusVotacao;
    }

    public static PautaResponse createFrom(Pauta pauta){
        return new PautaResponse(
                pauta.getId(),
                pauta.getDescricaoPauta(),
                pauta.getDataHoraAbeturaVotacao(),
                pauta.getDataHoraFechamentoVotacao(),
                pauta.getTotalVotosFavoraveis(),
                pauta.getTotalVotosContrarios(),
                pauta.getStatusVotacao()
            );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricaoPauta() {
        return descricaoPauta;
    }

    public void setDescricaoPauta(String descricaoPauta) {
        this.descricaoPauta = descricaoPauta;
    }

    public LocalDateTime getDataHoraAbeturaVotacao() {
        return dataHoraAbeturaVotacao;
    }

    public void setDataHoraAbeturaVotacao(LocalDateTime dataHoraAbeturaVotacao) {
        this.dataHoraAbeturaVotacao = dataHoraAbeturaVotacao;
    }

    public LocalDateTime getDataHoraFechamentoVotacao() {
        return dataHoraFechamentoVotacao;
    }

    public void setDataHoraFechamentoVotacao(LocalDateTime dataHoraFechamentoVotacao) {
        this.dataHoraFechamentoVotacao = dataHoraFechamentoVotacao;
    }

    public Long getTotalVotosFavoraveis() {
        return totalVotosFavoraveis;
    }

    public void setTotalVotosFavoraveis(Long totalVotosFavoraveis) {
        this.totalVotosFavoraveis = totalVotosFavoraveis;
    }

    public Long getTotalVotosContrarios() {
        return totalVotosContrarios;
    }

    public void setTotalVotosContrarios(Long totalVotosContrarios) {
        this.totalVotosContrarios = totalVotosContrarios;
    }

    public StatusVotacao getStatusVotacao() {
        return statusVotacao;
    }

    public void setStatusVotacao(StatusVotacao statusVotacao) {
        this.statusVotacao = statusVotacao;
    }
}
