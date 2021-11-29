package br.com.sicredi.dto;

import br.com.sicredi.enums.TipoVoto;
import io.swagger.v3.oas.annotations.media.Schema;

public class PautaVotoRequest {

    @Schema(description = "ID da Pauta", example = "61a42665c834c356bd4c3828")
    private String pauta_id;

    @Schema(description = "ID do Associado", example = "72a42665c834c356bd4c4025")
    private String associado_id;

    @Schema(description = "Tipo de voto do Associado", example = "SIM")
    private TipoVoto voto;

    public PautaVotoRequest(String pauta_id, String associado_id, TipoVoto voto) {
        this.pauta_id = pauta_id;
        this.associado_id = associado_id;
        this.voto = voto;
    }

    public PautaVotoRequest() {
    }

    public String getPauta_id() {
        return pauta_id;
    }

    public void setPauta_id(String pauta_id) {
        this.pauta_id = pauta_id;
    }

    public String getAssociado_id() {
        return associado_id;
    }

    public void setAssociado_id(String associado_id) {
        this.associado_id = associado_id;
    }

    public TipoVoto getVoto() {
        return voto;
    }

    public void setVoto(TipoVoto voto) {
        this.voto = voto;
    }
}
