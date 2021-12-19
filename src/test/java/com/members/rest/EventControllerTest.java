package com.members.rest;


import com.members.controller.UserController;
import com.members.controller.EventController;
import com.members.repository.EventRepository;
import com.members.model.Event;
import com.members.model.User;
import com.members.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = EventControllerTest.class)
public class EventControllerTest {

    @InjectMocks
    EventController eventController;

    @Mock
    EventRepository eventRepository;

    public String location1 = "Boston";
    public String location2 = "New York";
    public String location3 = "Chicago";
    public String eventName1 = "Carnival";
    public String eventName2 = "Parade";
    public String eventName3 = "Club";
    public String description1 = "So much fun!";
    public String description2 = "You'll have so much fun!";
    public String description3 = "It'll be so much fun!";


    @BeforeEach
    void init() {
        log.info("**** EventControllerTest - init");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetEventsSuccess() {
        log.info("**** EventControllerTest - testGetEventSuccess");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();
        Event event3 = Event.builder().description(description3).location(location3)
                .eventName(eventName3).eventDate(new Date()).build();

        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event3);

        doReturn(events).when(eventRepository).findAll();
        ResponseEntity<List<Event>>  events1 = eventController.getEvents();
        assertNotNull(events1);
    }

    @Test
    void testGetEventsFail() {
        log.info("**** EventControllerTest - testGetEventFail");

        doReturn(null).when(eventRepository).findAll();
        ResponseEntity<List<Event>>  eventResponse = eventController.getEvents();
        assertNotNull(eventResponse);
        log.info("u: ..{}..", eventResponse);
        assertEquals(HttpStatus.OK, eventResponse.getStatusCode());
        //List<Event> events = eventResponse.getBody();
        //assertEquals(0, events.size());
    }


    @Test
    void testCreateEventSuccess() {
        log.info("**** EventControllerTest - testGetEventSuccess");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();

        ResponseEntity<Event> event3 = eventController.createEvent(event1);
        log.info("e3: ..{}..", event3);
        log.info("e1: ..{}..", event1);

        assertEquals(event3.getBody(), event1);
    }

    @Test
    void testCreateEventFail() {
        log.info("**** EventControllerTest - testGetEventFail");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();
        try {
            ResponseEntity<Event> event3 = eventController.createEvent(null);
        } catch (Exception e) {
            log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null event"));
        }
    }

    @Test
    void testDeleteEventSuccess() {
        log.info("**** EventControllerTest - testDeleteEventSuccess");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();

        doReturn(Optional.of(event1)).when(eventRepository).findById(anyInt());

        ResponseEntity<Void> responseEntity = eventController.deleteEvent(1);
        log.info("322: ..{}..", responseEntity.toString());

       assertTrue(responseEntity.toString().contains("NO_CONTENT"));
       assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteEventFail() {
        log.info("**** EventControllerTest - testDeleteEventFail");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();

        doReturn(Optional.empty()).when(eventRepository).findById(anyInt());

        try {
            ResponseEntity<Void> responseEntity = eventController.deleteEvent(1);
        } catch (Exception e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("no event found"));
        }
        try {
            ResponseEntity<Void> responseEntity = eventController.deleteEvent(null);
        } catch (Exception e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null id"));
        }
    }

    @Test
    void testUpdateEventSuccess() {
        log.info("**** UserControllerTest - testUpdateUserSuccess");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();

        doReturn(Optional.of(event1)).when(eventRepository).findById(anyInt());

        ResponseEntity<Event> responseEntity = eventController.updateEvent(1, event1);
        log.info("e22: ..{}..", responseEntity.toString());

        log.info("e3: ..{}..", responseEntity);
        log.info("e1: ..{}..", event1);

        assertEquals(responseEntity.getBody(), event1);

        //testing a email non null
        doReturn(Optional.of(event1)).when(eventRepository).findById(anyInt());
        event1.setEventDate(null);
        event1.setLocation(null);
        event1.setEventName(null);

        ResponseEntity<Event> responseEntity2 = eventController.updateEvent(1, event1);
        assertEquals(responseEntity2.getBody(), event1);
        assertNull(responseEntity2.getBody().getEventName());
    }

    @Test
    void testUpdateEventFail() {
        log.info("**** EventControllerTest - testGetEventFail");
        Event event1 = Event.builder().description(description1).location(location1)
                .eventName(eventName1).eventDate(new Date()).build();
        //testing for null user
        try {
            ResponseEntity<Event> eventResponseEntity = eventController.updateEvent(1, null);
        } catch (ResponseStatusException e) {
            assertTrue(e.getMessage().contains("null event"));
        }
        //testing for null id
        try {
            ResponseEntity<Event> responseEntity = eventController.updateEvent(null, event1);
        } catch (ResponseStatusException e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null id"));
        }
        log.info("testing for event not found in the database");
        doReturn(Optional.empty()).when(eventRepository).findById(anyInt());
        try {
            ResponseEntity<Event> responseEntity = eventController.updateEvent(1, event1);
        } catch (ResponseStatusException e) {
            log.info("JOERR:{}" , e.getMessage());
            assertTrue(e.getMessage().contains("event with id:1 not found"));
        }

    }

}
