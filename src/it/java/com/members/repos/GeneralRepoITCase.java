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
  public void test_User() throws Exception {
    log.info("****** GeneralRepoITCase - test_User ***************");

    // CRUD
    List<User> users = userRepository.findAll();
    assertNotNull(users);


  }

}
