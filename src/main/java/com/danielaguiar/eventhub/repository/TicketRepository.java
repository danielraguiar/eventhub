package com.danielaguiar.eventhub.repository;

import com.danielaguiar.eventhub.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByParticipantEmail(String email);

    boolean existsByEventIdAndParticipantId(Long eventId, Long participantId);
}
