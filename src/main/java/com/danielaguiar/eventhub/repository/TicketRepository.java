package com.danielaguiar.eventhub.repository;

import com.danielaguiar.eventhub.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findByParticipantEmail(String email);

    boolean existsByEventIdAndParticipantId(UUID eventId, UUID participantId);
}
