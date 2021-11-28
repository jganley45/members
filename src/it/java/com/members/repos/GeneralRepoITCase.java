package com.members.repos;


import com.members.AbstractITCase;
import com.members.MembersApplication;
import com.members.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Tag("integration")
@SpringBootTest(classes = {MembersApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class GeneralRepoITCase extends AbstractITCase {

  String email1 = "sparker@dd.com";
  String loginId1 = "sparker";
  String name1 = "Sam Parker1";
  String name2 = "Joe Ganley";


  @BeforeAll
  public void init() {
    log.info("****** GeneralRepoITCase - INIT ***************");

    try {
      cleanupData();

      User user1 = User.builder().email(email1).name(name1)
              .loginId(loginId1).build();
      userRepository.saveAndFlush(user1);
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
}
