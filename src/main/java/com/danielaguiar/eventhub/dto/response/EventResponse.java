package com.danielaguiar.eventhub.dto.response;

import com.danielaguiar.eventhub.model.Event;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        LocalDateTime dateTime,
        String location,
        Integer capacity,
        Integer soldTickets,
        Integer availableCapacity
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDateTime(),
                event.getLocation(),
                event.getCapacity(),
                event.getSoldTickets(),
                event.availableCapacity()
        );
    }
}
