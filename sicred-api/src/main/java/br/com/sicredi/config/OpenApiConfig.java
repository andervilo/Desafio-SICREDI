package br.com.sicredi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Desafio SICREDI",
                version = "1.0",
                description = "Desafio SICREDI",
                license = @License(name = "Apache 2.0"),
                contact = @Contact(name = "Anderson Oliveira", email = "andervilo@gmail.com")
        )
)
@Configuration
public class OpenApiConfig {
}
