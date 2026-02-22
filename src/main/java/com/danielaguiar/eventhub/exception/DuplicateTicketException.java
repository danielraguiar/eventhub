package com.danielaguiar.eventhub.exception;

public class DuplicateTicketException extends RuntimeException {

    public DuplicateTicketException(String email, Long eventId) {
        super("Participant with email '" + email + "' already has a ticket for event with id " + eventId);
    }
}
