package com.danielaguiar.eventhub.repository;

import com.danielaguiar.eventhub.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
