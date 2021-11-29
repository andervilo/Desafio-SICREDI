package br.com.sicredi.dto;

import br.com.sicredi.models.Associado;
import io.swagger.v3.oas.annotations.media.Schema;

public class AssociadoResponse {

    @Schema(description = "ID do Associado", example = "61a42665c834c356bd4c3828")
    private String id;

    @Schema(description = "Nome do Associado", example = "Jessica Abigail")
    private String nome;

    @Schema(description = "CPF do Associado", example = "Jessica Abigail")
    private String cpf;

    public AssociadoResponse(String id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public static AssociadoResponse createFrom(Associado associado) {
        return new AssociadoResponse(associado.getId(), associado.getNome(), associado.getCpf());
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }
}
