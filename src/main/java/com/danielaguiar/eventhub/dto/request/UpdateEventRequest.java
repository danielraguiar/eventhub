package com.danielaguiar.eventhub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateEventRequest(

        @NotBlank(message = "Event name must not be blank")
        @Schema(example = "Tech Conference 2026")
        String name,

        @NotNull(message = "Date and time must not be null")
        @Future(message = "Event date must be in the future")
        @Schema(example = "2026-08-15T09:00:00")
        LocalDateTime dateTime,

        @NotBlank(message = "Location must not be blank")
        @Schema(example = "São Paulo Convention Center")
        String location,

        @NotNull(message = "Capacity must not be null")
        @Positive(message = "Capacity must be a positive number")
        @Schema(example = "200")
        Integer capacity
) {
}
