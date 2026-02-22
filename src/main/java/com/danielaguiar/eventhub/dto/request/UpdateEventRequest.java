package com.danielaguiar.eventhub.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateEventRequest(

        @NotBlank(message = "Event name must not be blank")
        String name,

        @NotNull(message = "Date and time must not be null")
        @Future(message = "Event date must be in the future")
        LocalDateTime dateTime,

        @NotBlank(message = "Location must not be blank")
        String location,

        @NotNull(message = "Capacity must not be null")
        @Positive(message = "Capacity must be a positive number")
        Integer capacity
) {
}
