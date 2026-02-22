package com.danielaguiar.eventhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseTicketRequest(

        @NotNull(message = "Event ID must not be null")
        Long eventId,

        @NotBlank(message = "Participant name must not be blank")
        String participantName,

        @NotBlank(message = "Participant email must not be blank")
        @Email(message = "Participant email must be a valid email address")
        String participantEmail
) {
}
