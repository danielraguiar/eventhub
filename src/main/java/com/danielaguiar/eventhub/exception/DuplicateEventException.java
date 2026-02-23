package com.danielaguiar.eventhub.exception;

import java.time.LocalDateTime;

public class DuplicateEventException extends RuntimeException {

    public DuplicateEventException(String name, String location, LocalDateTime dateTime) {
        super("Já existe um evento com o nome \"%s\" no local \"%s\" na data %s.".formatted(name, location, dateTime));
    }
}
