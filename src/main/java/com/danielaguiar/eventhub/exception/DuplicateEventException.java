package com.danielaguiar.eventhub.exception;

import java.time.LocalDateTime;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String name, String location, LocalDateTime dateTime) {
        super("An event named \"%s\" already exists at \"%s\" on %s.".formatted(name, location, dateTime));
    }
}
