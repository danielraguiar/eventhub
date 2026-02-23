package com.danielaguiar.eventhub.exception;

import java.util.UUID;

public class EventFullException extends RuntimeException {

    public EventFullException(UUID eventId) {
        super("Event with id " + eventId + " is at full capacity. No tickets available.");
    }
}
