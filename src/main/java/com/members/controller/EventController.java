package com.members.controller;

import com.members.model.Event;
import com.members.model.User;
import com.members.repository.EventRepository;
import com.members.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequestMapping(path = {"/event"})
@RestController
public class EventController {
    @Autowired
    EventRepository eventRepository;

    @GetMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
           path = {"get-events"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Event>> getEvents() {
        log.info("Here in get-events");
       List<Event> events = eventRepository.findAll();
       return ResponseEntity.ok(events);
    }


    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE,
          path = "event")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (Objects.isNull(event)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null event");
        }
        log.info("eventjoe: {}", event);
        eventRepository.saveAndFlush(event);
        log.info("eventjoe2: {}", event);
        return ResponseEntity.ok(event);
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "event/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Integer id) {
        log.info("id1: {}", id);
        if (Objects.isNull(id)) {
            throw new RuntimeException("null id");
        }
        log.info("id2: {}", id);
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("null id"));
        }
        eventRepository.delete(event.get());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "event/{id}", consumes = "application/json")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Integer id, @RequestBody Event event) {
        log.info("id1: {}", id);
        if (Objects.isNull(event)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null event");
        }
        if (Objects.isNull(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null id");
        }
        log.info("id2: {}", id);
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            // https://www.baeldung.com/spring-response-status-exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("event with id:%d not found", id));
        }
        log.info("event: {}", event);
        if (Objects.nonNull(event.getEventDate())) {
            eventOptional.get().setEventDate(event.getEventDate());
        }
        if (Objects.nonNull(event.getEventName())) {
            eventOptional.get().setEventName(event.getEventName());
        }
        if (Objects.nonNull(event.getDescription())) {
            eventOptional.get().setDescription(event.getDescription());
        }
        if (Objects.nonNull(event.getLocation())) {
            eventOptional.get().setLocation(event.getLocation());
        }
        eventRepository.saveAndFlush(eventOptional.get());
        return ResponseEntity.ok(eventOptional.get());
    }


}
