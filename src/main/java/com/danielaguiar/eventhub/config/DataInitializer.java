package com.danielaguiar.eventhub.config;

import com.danielaguiar.eventhub.model.Event;
import com.danielaguiar.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    @Override
    public void run(String... args) {
        if (eventRepository.count() > 0) {
            return;
        }

        eventRepository.saveAll(List.of(
                Event.builder()
                        .name("Tech Conference 2026")
                        .dateTime(LocalDateTime.of(2026, 8, 15, 9, 0))
                        .location("São Paulo Convention Center")
                        .capacity(200)
                        .soldTickets(0)
                        .build(),
                Event.builder()
                        .name("Java Dev Summit")
                        .dateTime(LocalDateTime.of(2026, 9, 20, 10, 0))
                        .location("Rio de Janeiro Expo")
                        .capacity(150)
                        .soldTickets(0)
                        .build(),
                Event.builder()
                        .name("Spring Boot Workshop")
                        .dateTime(LocalDateTime.of(2026, 10, 5, 14, 0))
                        .location("Online")
                        .capacity(50)
                        .soldTickets(0)
                        .build()
        ));
    }
}
