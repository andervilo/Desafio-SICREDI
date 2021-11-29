package br.com.sicredi.dto;

import br.com.sicredi.enums.TipoPeriodoVotacao;
import io.swagger.v3.oas.annotations.media.Schema;

public class PautaIniciarVotacaoRequest {

    @Schema(description = "ID da Pauta", example = "61a42665c834c356bd4c3828")
    private String pautaId;

    @Schema(description = "Tipo de Período para votação", example = "MINUTO")
    private TipoPeriodoVotacao tipoPeriodoVotacao;

    @Schema(description = "Duração do período de votação", example = "10")
    private long duracao;

    public PautaIniciarVotacaoRequest(String pautaId, TipoPeriodoVotacao tipoPeriodoVotacao, Integer duracao) {
        this.pautaId = pautaId;
        this.tipoPeriodoVotacao = tipoPeriodoVotacao;
        this.duracao = duracao;
    }

    public PautaIniciarVotacaoRequest() {
    }

    public String getPautaId() {
        return pautaId;
    }

    public void setPautaId(String pautaId) {
        this.pautaId = pautaId;
    }

    public TipoPeriodoVotacao getTipoPeriodoVotacao() {
        return tipoPeriodoVotacao;
    }

    public void setTipoPeriodoVotacao(TipoPeriodoVotacao tipoPeriodoVotacao) {
        this.tipoPeriodoVotacao = tipoPeriodoVotacao;
    }

    public long getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }
}
