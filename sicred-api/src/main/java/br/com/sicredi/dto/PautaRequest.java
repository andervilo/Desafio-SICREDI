package br.com.sicredi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PautaRequest {

    @Schema(description = "Descrição da Pauta")
    private String descricaoPauta;

    public PautaRequest() {
    }

    public String getDescricaoPauta() {
        return descricaoPauta;
    }

    public void setDescricaoPauta(String descricaoPauta) {
        this.descricaoPauta = descricaoPauta;
    }
}
