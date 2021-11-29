package br.com.sicredi.services;

import br.com.sicredi.dto.ValidarCpfResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidarCpfImpl implements ValidarCpf {

    private static final String API_REQUEST = "https://user-info.herokuapp.com/users";

    private static final String CPF_INVALID_MESSAGE = "CPF inválido!";

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public ValidarCpfResponse validarCpf(String cpf) {

        ResponseEntity<ValidarCpfResponse> response = null;

        try {
            response = restTemplate.getForEntity(API_REQUEST + "/" + cpf, ValidarCpfResponse.class);
        }catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CPF_INVALID_MESSAGE);
        }catch (RestClientException e) {
            e.printStackTrace();
        }

        return response.getBody();
    }


}
