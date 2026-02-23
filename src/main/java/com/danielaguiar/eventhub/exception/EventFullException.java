package com.danielaguiar.eventhub.exception;

import java.util.UUID;

public class EventFullException extends RuntimeException {

    public EventFullException(UUID eventId) {
        super("O evento com id " + eventId + " está com capacidade esgotada. Não há ingressos disponíveis.");
    }
}
