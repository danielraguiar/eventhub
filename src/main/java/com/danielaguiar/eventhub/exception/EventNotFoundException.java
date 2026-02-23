package com.danielaguiar.eventhub.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(UUID id) {
        super("Evento não encontrado com id: " + id);
    }
}
