package com.danielaguiar.eventhub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PurchaseTicketRequest(

        @NotNull(message = "Event ID must not be null")
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID eventId,

        @NotBlank(message = "Participant name must not be blank")
        @Schema(example = "Alice Silva")
        String participantName,

        @NotBlank(message = "Participant email must not be blank")
        @Email(message = "Participant email must be a valid email address")
        @Schema(example = "alice@example.com")
        String participantEmail
) {
}
