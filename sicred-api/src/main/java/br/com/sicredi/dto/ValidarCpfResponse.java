package br.com.sicredi.dto;

import br.com.sicredi.enums.StatusCpf;

public class ValidarCpfResponse {
    private String status;

    public ValidarCpfResponse(String status) {
        this.status = status;
    }

    public ValidarCpfResponse() {
    }

    public StatusCpf getStatus() {
        return StatusCpf.valueOf(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
