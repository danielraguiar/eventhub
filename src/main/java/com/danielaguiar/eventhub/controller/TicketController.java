package com.danielaguiar.eventhub.controller;

import com.danielaguiar.eventhub.dto.request.PurchaseTicketRequest;
import com.danielaguiar.eventhub.dto.response.TicketResponse;
import com.danielaguiar.eventhub.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> purchase(@RequestBody @Valid PurchaseTicketRequest request) {
        TicketResponse response = ticketService.purchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/participant/{email}")
    public ResponseEntity<List<TicketResponse>> getByParticipantEmail(@PathVariable String email) {
        return ResponseEntity.ok(ticketService.findByParticipantEmail(email));
    }
}
