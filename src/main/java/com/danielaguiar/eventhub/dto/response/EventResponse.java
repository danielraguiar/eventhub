package com.danielaguiar.eventhub.dto.response;

import com.danielaguiar.eventhub.model.Event;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(example = "Tech Conference 2026")
        String name,
        @Schema(example = "2026-08-15T09:00:00")
        LocalDateTime dateTime,
        @Schema(example = "São Paulo Convention Center")
        String location,
        @Schema(example = "200")
        Integer capacity,
        @Schema(example = "0")
        Integer soldTickets,
        @Schema(example = "200")
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
