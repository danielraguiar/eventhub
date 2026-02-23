package com.danielaguiar.eventhub.repository;

import com.danielaguiar.eventhub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByNameAndDateTimeAndLocation(String name, LocalDateTime dateTime, String location);

    boolean existsByNameAndDateTimeAndLocationAndIdNot(String name, LocalDateTime dateTime, String location, Long id);
}
