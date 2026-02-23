package com.danielaguiar.eventhub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PurchaseTicketRequest(

        @NotNull(message = "O ID do evento não pode ser nulo")
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID eventId,

        @NotBlank(message = "O nome do participante não pode estar em branco")
        @Schema(example = "Alice Silva")
        String participantName,

        @NotBlank(message = "O e-mail do participante não pode estar em branco")
        @Email(message = "O e-mail do participante deve ser um endereço válido")
        @Schema(example = "alice@example.com")
        String participantEmail
) {
}
