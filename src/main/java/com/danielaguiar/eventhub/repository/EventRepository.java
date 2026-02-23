package com.danielaguiar.eventhub.repository;

import com.danielaguiar.eventhub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    boolean existsByNameAndDateTimeAndLocation(String name, LocalDateTime dateTime, String location);

    boolean existsByNameAndDateTimeAndLocationAndIdNot(String name, LocalDateTime dateTime, String location, UUID id);
}
