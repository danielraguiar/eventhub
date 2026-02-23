package com.danielaguiar.eventhub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateEventRequest(

        @NotBlank(message = "O nome do evento não pode estar em branco")
        @Schema(example = "Tech Conference 2026")
        String name,

        @NotNull(message = "A data e horário não podem ser nulos")
        @Future(message = "A data do evento deve ser no futuro")
        @Schema(example = "2026-08-15T09:00:00")
        LocalDateTime dateTime,

        @NotBlank(message = "O local não pode estar em branco")
        @Schema(example = "São Paulo Convention Center")
        String location,

        @NotNull(message = "A capacidade não pode ser nula")
        @Positive(message = "A capacidade deve ser um número positivo")
        @Schema(example = "200")
        Integer capacity
) {
}
