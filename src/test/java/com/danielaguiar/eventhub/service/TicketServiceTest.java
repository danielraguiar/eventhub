package com.danielaguiar.eventhub.service;

import com.danielaguiar.eventhub.dto.request.PurchaseTicketRequest;
import com.danielaguiar.eventhub.dto.response.TicketResponse;
import com.danielaguiar.eventhub.exception.DuplicateTicketException;
import com.danielaguiar.eventhub.exception.EventFullException;
import com.danielaguiar.eventhub.exception.EventNotFoundException;
import com.danielaguiar.eventhub.model.Event;
import com.danielaguiar.eventhub.model.Participant;
import com.danielaguiar.eventhub.model.Ticket;
import com.danielaguiar.eventhub.repository.ParticipantRepository;
import com.danielaguiar.eventhub.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketService unit tests")
class TicketServiceTest {

    @Mock
    private EventService eventService;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Event buildEvent(int capacity, int soldTickets) {
        return Event.builder()
                .id(1L)
                .name("Test Event")
                .dateTime(LocalDateTime.now().plusDays(10))
                .location("São Paulo")
                .capacity(capacity)
                .soldTickets(soldTickets)
                .build();
    }

    private Participant buildParticipant() {
        return Participant.builder()
                .id(10L)
                .name("Alice")
                .email("alice@example.com")
                .build();
    }

    private PurchaseTicketRequest buildRequest() {
        return new PurchaseTicketRequest(1L, "Alice", "alice@example.com");
    }

    @Test
    @DisplayName("purchase: when event has capacity, creates ticket and returns DTO")
    void purchase_whenEventHasCapacity_createsTicketAndReturnsDTO() {
        Event event = buildEvent(10, 5);
        Participant participant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(participant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> {
            Ticket t = inv.getArgument(0);
            t.setId(100L);
            return t;
        });

        TicketResponse response = ticketService.purchase(buildRequest());

        assertThat(response.eventId()).isEqualTo(1L);
        assertThat(response.participantEmail()).isEqualTo("alice@example.com");
        assertThat(response.purchasedAt()).isNotNull();
    }

    @Test
    @DisplayName("purchase: when last slot is available, purchase succeeds at boundary")
    void purchase_whenLastSlotAvailable_succeedsAtBoundary() {
        Event event = buildEvent(5, 4);
        Participant participant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(participant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        TicketResponse response = ticketService.purchase(buildRequest());

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("purchase: when event is full, throws EventFullException")
    void purchase_whenEventIsFull_throwsEventFullException() {
        Event event = buildEvent(5, 5);

        given(eventService.findEntityById(1L)).willReturn(event);

        assertThatThrownBy(() -> ticketService.purchase(buildRequest()))
                .isInstanceOf(EventFullException.class)
                .hasMessageContaining("1");

        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("purchase: when event capacity is zero, throws EventFullException")
    void purchase_whenCapacityIsZero_throwsEventFullException() {
        Event event = buildEvent(0, 0);

        given(eventService.findEntityById(1L)).willReturn(event);

        assertThatThrownBy(() -> ticketService.purchase(buildRequest()))
                .isInstanceOf(EventFullException.class);

        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("purchase: when event does not exist, throws EventNotFoundException")
    void purchase_whenEventNotFound_throwsEventNotFoundException() {
        given(eventService.findEntityById(1L)).willThrow(new EventNotFoundException(1L));

        assertThatThrownBy(() -> ticketService.purchase(buildRequest()))
                .isInstanceOf(EventNotFoundException.class);

        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("purchase: when participant email is new, creates new participant")
    void purchase_whenParticipantIsNew_createsNewParticipant() {
        Event event = buildEvent(10, 0);
        Participant newParticipant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.empty());
        given(participantRepository.save(any(Participant.class))).willReturn(newParticipant);
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        ticketService.purchase(buildRequest());

        ArgumentCaptor<Participant> captor = ArgumentCaptor.forClass(Participant.class);
        verify(participantRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("alice@example.com");
        assertThat(captor.getValue().getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("purchase: when participant already exists, reuses existing participant")
    void purchase_whenParticipantExists_reusesExistingParticipant() {
        Event event = buildEvent(10, 0);
        Participant existingParticipant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(existingParticipant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        ticketService.purchase(buildRequest());

        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    @DisplayName("purchase: when participant already has a ticket for the event, throws DuplicateTicketException")
    void purchase_whenDuplicatePurchase_throwsDuplicateTicketException() {
        Event event = buildEvent(10, 1);
        Participant participant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(participant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(true);

        assertThatThrownBy(() -> ticketService.purchase(buildRequest()))
                .isInstanceOf(DuplicateTicketException.class)
                .hasMessageContaining("alice@example.com");

        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("purchase: when purchased, increments soldTickets on the event")
    void purchase_whenPurchased_incrementsSoldTickets() {
        Event event = buildEvent(10, 3);
        Participant participant = buildParticipant();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(participant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        ticketService.purchase(buildRequest());

        assertThat(event.getSoldTickets()).isEqualTo(4);
    }

    @Test
    @DisplayName("purchase: when purchased, sets purchasedAt to a non-null recent timestamp")
    void purchase_whenPurchased_setsPurchasedAtToNow() {
        Event event = buildEvent(10, 0);
        Participant participant = buildParticipant();
        LocalDateTime before = LocalDateTime.now();

        given(eventService.findEntityById(1L)).willReturn(event);
        given(participantRepository.findByEmail("alice@example.com")).willReturn(Optional.of(participant));
        given(ticketRepository.existsByEventIdAndParticipantId(1L, 10L)).willReturn(false);
        given(ticketRepository.save(any(Ticket.class))).willAnswer(inv -> inv.getArgument(0));

        TicketResponse response = ticketService.purchase(buildRequest());

        assertThat(response.purchasedAt()).isNotNull();
        assertThat(response.purchasedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("findByParticipantEmail: when participant has tickets, returns mapped list")
    void findByParticipantEmail_whenHasTickets_returnsList() {
        Event event = buildEvent(10, 2);
        Participant participant = buildParticipant();

        Ticket t1 = Ticket.builder().id(1L).event(event).participant(participant)
                .purchasedAt(LocalDateTime.now()).build();
        Ticket t2 = Ticket.builder().id(2L).event(event).participant(participant)
                .purchasedAt(LocalDateTime.now()).build();

        given(ticketRepository.findByParticipantEmail("alice@example.com")).willReturn(List.of(t1, t2));

        List<TicketResponse> result = ticketService.findByParticipantEmail("alice@example.com");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).participantEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("findByParticipantEmail: when participant has no tickets, returns empty list")
    void findByParticipantEmail_whenNoTickets_returnsEmptyList() {
        given(ticketRepository.findByParticipantEmail("unknown@example.com")).willReturn(List.of());

        List<TicketResponse> result = ticketService.findByParticipantEmail("unknown@example.com");

        assertThat(result).isEmpty();
    }
}
