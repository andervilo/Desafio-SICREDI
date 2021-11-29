package br.com.sicredi.dto;

import br.com.sicredi.models.Associado;
import io.swagger.v3.oas.annotations.media.Schema;

public class AssociadoRequest {

    @Schema(description = "Nome do Associado", example = "Jessica Abigail", required = true)
    private String nome;

    @Schema(description = "CPF do Associado", example = "99999999999", required = true)
    private String cpf;

    public AssociadoRequest() {
    }

    public Associado creareTo() {
        return new Associado(getNome(), getCpf());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
