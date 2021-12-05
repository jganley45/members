package com.members.rest;


import com.members.controller.UserController;
import com.members.model.User;
import com.members.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = UserControllerTest.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    String email1 = "sparker@dd.com";
    String loginId1 = "sparker";
    String name1 = "Sam Parker1";
    String email2 = "kerry@dd.com";
    String loginId2 = "kerry";
    String name2 = "Kerry O";


    @BeforeEach
    void init() {
        log.info("**** UserControllerTest - init");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUsersSuccess() {
        log.info("**** UserControllerTest - testGetUsersSuccess");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();
        User user2 = User.builder().email(email2).name(name2).loginId(loginId2).build();
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        doReturn(users).when(userRepository).findAll();
        ResponseEntity<List<User>>  users1 = userController.getUsers();
        assertNotNull(users1);
    }

    @Test
    void testGetUsersFail() {
        log.info("**** UserControllerTest - testGetUsersFail");

        doReturn(null).when(userRepository).findAll();
        ResponseEntity<List<User>>  userResponse = userController.getUsers();
        assertNotNull(userResponse);
        log.info("u: ..{}..", userResponse);
        assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        //List<User> users = userResponse.getBody();
        //assertEquals(0, users.size());
    }
}
