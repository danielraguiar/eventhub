package com.danielaguiar.eventhub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EventHub API")
                        .description("API RESTful para gestão de eventos e venda de ingressos")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Daniel Aguiar")
                                .email("danielramos.aguiar@gmail.com")));
    }
}
