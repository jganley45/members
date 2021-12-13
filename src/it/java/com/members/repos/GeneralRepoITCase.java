package com.members.repos;


import com.members.AbstractITCase;
import com.members.MembersApplication;
import com.members.model.Event;
import com.members.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Tag("integration")
@SpringBootTest(classes = {MembersApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class GeneralRepoITCase extends AbstractITCase {



  @BeforeAll
  public void init() {
    log.info("****** GeneralRepoITCase - INIT ***************");

    try {
      cleanupData();

      User user1 = User.builder().email(email1).name(name1)
              .loginId(loginId1).build();
      userRepository.saveAndFlush(user1);


      Event event1 = Event.builder().description(description1).location(location1)
              .eventName(eventName1).eventDate(new Date()).build();

      eventRepository.saveAndFlush(event1);

    } catch (Exception e) {
      // ** catch any exception and FAIL
      log.error("GeneralRepoITCase @BEFORE ALL - General Exception occurred" + e);
      e.printStackTrace();
      return;
    }
  }


  @Test
  @Order(1)
  public void test_FindUser() throws Exception {
    log.info("****** GeneralRepoITCase - test_FindUser ***************");

    // CRUD
    List<User> users = userRepository.findAll();
    assertNotNull(users);


  }

  @Test
  @Order(4)
  public void test_UpdateName() {
    Optional<User> userOptional = userRepository.findByName(name1);

    User u = userOptional.get();
    String name = u.getName();
    assertEquals(name1, name);
    u.setName(name2);

    User savedUser = userRepository.saveAndFlush(u);
    assertNotNull(savedUser);

    assertEquals(name2, savedUser.getName());
  }

  @Test
  @Order(5)
  public void test_DeleteByName() {
    Optional<User> userOptional = userRepository.findByName(name2);

    assertTrue(userOptional.isPresent());
    userRepository.delete(userOptional.get());

    Optional<User> userOptional2 = userRepository.findByName(name2);
    assertTrue(userOptional2.isEmpty());


  }

  @Test
  @Order(2)
  public void test_LoginID() throws Exception {
    log.info("****** GeneralRepoITCase - test_LoginID ***************");

    // CRUD
    Optional<User> users = userRepository.findByLoginId(loginId1);
    assertTrue(users.isPresent());
    assertNotNull(users.get());

  }

  @Test
  @Order(3)
  public void test_Email() throws Exception {
    log.info("****** GeneralRepoITCase - test_Email ***************");
    Optional<User> user = userRepository.findByEmail(email1);
    assertTrue(user.isPresent());
    assertNotNull(user.get());
    user = userRepository.findByEmail("joe@ganleys.com");
    assertTrue(user.isEmpty());
  }

  @Test
  @Order(1)
  public void test_eventCrud() throws Exception {
    log.info("****** GeneralRepoITCase - test_FindEvent ***************");

    // CRUD
    List<Event> events = eventRepository.findAll();
    assertNotNull(events);

    log.info("****** GeneralRepoITCase - test_UpdateEvent ***************");
    Optional<Event> eventOptional = eventRepository.findByEventName(eventName1);
    assertTrue(eventOptional.isPresent());
    Event e = eventOptional.get();
    String name = e.getEventName();
    assertEquals(eventName1, name);
    e.setEventName(eventName2);
    Event savedEvent = eventRepository.saveAndFlush(e);
    assertNotNull(savedEvent);
    assertEquals(eventName2, savedEvent.getEventName());




    log.info("****** GeneralRepoITCase - test_CreateEvent ***************");
    Event event3 = Event.builder().description(description3).location(location3)
            .eventName(eventName3).eventDate(new Date()).build();

    savedEvent = eventRepository.saveAndFlush(event3);
    assertNotNull(savedEvent);
    assertEquals(event3, savedEvent);

    List<Event> events2 = eventRepository.findAll();
    assertEquals(2, events2.size());

    log.info("****** GeneralRepoITCase - test_DeleteEvent ***************");
    Optional<Event> eventOptional2 = eventRepository.findByEventName(eventName2);

    assertTrue(eventOptional2.isPresent());
    eventRepository.delete(eventOptional2.get());

    Optional<Event> eventOptional3 = eventRepository.findByEventName(eventName2);
    assertTrue(eventOptional3.isEmpty());

    List<Event> events3 = eventRepository.findAll();
    assertEquals(1, events3.size());



  }




}
