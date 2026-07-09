package com.infobip.pmf.course.runclub.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public Events getAll() {
        return new Events(eventService.getAll());
    }

    @GetMapping("/events/{id}")
    public Event getOne(@PathVariable Long id) {
        return eventService.getOne(id);
    }

    @GetMapping("/clubs/{clubId}/events")
    public Events getForClub(@PathVariable Long clubId) {
        return new Events(eventService.getForClub(clubId));
    }

    @PostMapping("/clubs/{clubId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@PathVariable Long clubId, @Valid @NotNull @RequestBody CreateEventRequest request) {
        return eventService.create(clubId, request);
    }
}
