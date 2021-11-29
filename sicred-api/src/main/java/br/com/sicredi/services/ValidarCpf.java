package br.com.sicredi.services;

import br.com.sicredi.dto.ValidarCpfResponse;
import br.com.sicredi.enums.StatusCpf;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public interface ValidarCpf {

    ValidarCpfResponse validarCpf(String cpf);
}
