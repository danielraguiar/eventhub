package com.danielaguiar.eventhub.service;

import com.danielaguiar.eventhub.dto.request.PurchaseTicketRequest;
import com.danielaguiar.eventhub.dto.response.TicketResponse;
import com.danielaguiar.eventhub.exception.DuplicateTicketException;
import com.danielaguiar.eventhub.exception.EventFullException;
import com.danielaguiar.eventhub.model.Event;
import com.danielaguiar.eventhub.model.Participant;
import com.danielaguiar.eventhub.model.Ticket;
import com.danielaguiar.eventhub.repository.ParticipantRepository;
import com.danielaguiar.eventhub.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final EventService eventService;
    private final ParticipantRepository participantRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public TicketResponse purchase(PurchaseTicketRequest request) {
        Event event = eventService.findEntityById(request.eventId());

        if (event.availableCapacity() <= 0) {
            throw new EventFullException(event.getId());
        }

        Participant participant = findOrCreateParticipant(request.participantEmail(), request.participantName());

        if (ticketRepository.existsByEventIdAndParticipantId(event.getId(), participant.getId())) {
            throw new DuplicateTicketException(participant.getEmail(), event.getId());
        }

        Ticket ticket = Ticket.builder()
                .event(event)
                .participant(participant)
                .purchasedAt(LocalDateTime.now())
                .build();

        event.setSoldTickets(event.getSoldTickets() + 1);

        ticketRepository.save(ticket);

        return TicketResponse.from(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findByParticipantEmail(String email) {
        return ticketRepository.findByParticipantEmail(email)
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    private Participant findOrCreateParticipant(String email, String name) {
        return participantRepository.findByEmail(email)
                .orElseGet(() -> participantRepository.save(
                        Participant.builder()
                                .name(name)
                                .email(email)
                                .build()
                ));
    }
}
