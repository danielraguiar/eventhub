package com.danielaguiar.eventhub.service;

import com.danielaguiar.eventhub.dto.request.CreateEventRequest;
import com.danielaguiar.eventhub.dto.request.UpdateEventRequest;
import com.danielaguiar.eventhub.dto.response.EventResponse;
import com.danielaguiar.eventhub.exception.DuplicateEventException;
import com.danielaguiar.eventhub.exception.EventNotFoundException;
import com.danielaguiar.eventhub.model.Event;
import com.danielaguiar.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        return EventResponse.from(findEntityById(id));
    }

    @Transactional
    public EventResponse create(CreateEventRequest request) {
        if (eventRepository.existsByNameAndDateTimeAndLocation(request.name(), request.dateTime(), request.location())) {
            throw new DuplicateEventException(request.name(), request.location(), request.dateTime());
        }

        Event event = Event.builder()
                .name(request.name())
                .dateTime(request.dateTime())
                .location(request.location())
                .capacity(request.capacity())
                .soldTickets(0)
                .build();

        return EventResponse.from(eventRepository.save(event));
    }

    @Transactional
    public EventResponse update(Long id, UpdateEventRequest request) {
        Event event = findEntityById(id);

        if (eventRepository.existsByNameAndDateTimeAndLocationAndIdNot(request.name(), request.dateTime(), request.location(), id)) {
            throw new DuplicateEventException(request.name(), request.location(), request.dateTime());
        }

        int soldTickets = event.getSoldTickets();
        if (request.capacity() < soldTickets) {
            throw new IllegalArgumentException(
                    "New capacity (%d) cannot be less than tickets already sold (%d)"
                            .formatted(request.capacity(), soldTickets));
        }

        event.setName(request.name());
        event.setDateTime(request.dateTime());
        event.setLocation(request.location());
        event.setCapacity(request.capacity());

        return EventResponse.from(eventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException(id);
        }
        eventRepository.deleteById(id);
    }

    Event findEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }
}
