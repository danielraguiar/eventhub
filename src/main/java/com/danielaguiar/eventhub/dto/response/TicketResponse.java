package com.danielaguiar.eventhub.dto.response;

import com.danielaguiar.eventhub.model.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        @Schema(example = "7c9e6679-7425-40de-944b-e07fc1f90ae7")
        UUID ticketId,
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID eventId,
        @Schema(example = "Tech Conference 2026")
        String eventName,
        @Schema(example = "Alice Silva")
        String participantName,
        @Schema(example = "alice@example.com")
        String participantEmail,
        @Schema(example = "2026-08-15T09:05:00")
        LocalDateTime purchasedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getEvent().getId(),
                ticket.getEvent().getName(),
                ticket.getParticipant().getName(),
                ticket.getParticipant().getEmail(),
                ticket.getPurchasedAt()
        );
    }
}
