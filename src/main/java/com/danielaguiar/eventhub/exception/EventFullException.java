package com.danielaguiar.eventhub.exception;

public class EventFullException extends RuntimeException {

    public EventFullException(Long eventId) {
        super("Event with id " + eventId + " is at full capacity. No tickets available.");
    }
}
