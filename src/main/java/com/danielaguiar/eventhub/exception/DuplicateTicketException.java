package com.danielaguiar.eventhub.exception;

import java.util.UUID;

public class DuplicateTicketException extends RuntimeException {

    public DuplicateTicketException(String email, UUID eventId) {
        super("Participante com e-mail '" + email + "' já possui ingresso para o evento com id " + eventId);
    }
}
