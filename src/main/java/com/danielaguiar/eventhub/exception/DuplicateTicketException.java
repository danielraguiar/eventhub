package com.danielaguiar.eventhub.exception;

import java.util.UUID;

public class DuplicateTicketException extends RuntimeException {

    public DuplicateTicketException(String email, UUID eventId) {
        super("Participant with email '" + email + "' already has a ticket for event with id " + eventId);
    }
}
