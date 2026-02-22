package com.danielaguiar.eventhub.dto.response;

import com.danielaguiar.eventhub.model.Ticket;

import java.time.LocalDateTime;

public record TicketResponse(
        Long ticketId,
        Long eventId,
        String eventName,
        String participantName,
        String participantEmail,
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
