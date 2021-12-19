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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
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


    @Test
    void testCreateUserSuccess() {
        log.info("**** UserControllerTest - testGetUserSuccess");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();

        ResponseEntity<User> user3 = userController.createUser(user1);
        log.info("u3: ..{}..", user3);
        log.info("u1: ..{}..", user1);

        assertEquals(user3.getBody(), user1);
    }

    @Test
    void testCreateUserFail() {
        log.info("**** UserControllerTest - testGetUserFail");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();
        try {
            ResponseEntity<User> user3 = userController.createUser(null);
        } catch (Exception e) {
            log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null user"));
        }
    }

    @Test
    void testDeleteUserSuccess() {
        log.info("**** UserControllerTest - testDeleteUserSuccess");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();

        doReturn(Optional.of(user1)).when(userRepository).findById(anyInt());

        ResponseEntity<Void> responseEntity = userController.deleteUser(1);
        log.info("u22: ..{}..", responseEntity.toString());

       assertTrue(responseEntity.toString().contains("NO_CONTENT"));
       assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteUserFail() {
        log.info("**** UserControllerTest - testDeleteUserFail");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();

        doReturn(Optional.empty()).when(userRepository).findById(anyInt());

        try {
            ResponseEntity<Void> responseEntity = userController.deleteUser(1);
        } catch (Exception e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("no user found"));
        }
        try {
            ResponseEntity<Void> responseEntity = userController.deleteUser(null);
        } catch (Exception e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null id"));
        }
    }

    @Test
    void testUpdateUserSuccess() {
        log.info("**** UserControllerTest - testUpdateUserSuccess");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();

        doReturn(Optional.of(user1)).when(userRepository).findById(anyInt());

        ResponseEntity<User> responseEntity = userController.updateUser(1, user1);
        log.info("u22: ..{}..", responseEntity.toString());

        log.info("u3: ..{}..", responseEntity);
        log.info("u1: ..{}..", user1);

        assertEquals(responseEntity.getBody(), user1);

        //testing a email non null
        doReturn(Optional.of(user1)).when(userRepository).findById(anyInt());
        user1.setEmail(null);
        user1.setName(null);
        user1.setLoginId(null);

        ResponseEntity<User> responseEntity2 = userController.updateUser(1, user1);
        assertEquals(responseEntity2.getBody(), user1);
        assertNull(responseEntity2.getBody().getName());



    }

    @Test
    void testUpdateUserFail() {
        log.info("**** UserControllerTest - testGetUserFail");
        User user1 = User.builder().email(email1).name(name1).loginId(loginId1).build();
        //testing for null user
        try {
            ResponseEntity<User> responseEntity = userController.updateUser(1, null);
        } catch (ResponseStatusException e) {
            assertTrue(e.getMessage().contains("null user"));
        }
        //testing for null id
        try {
            ResponseEntity<User> responseEntity = userController.updateUser(null, user1);
        } catch (ResponseStatusException e) {
            //log.info("C: {} {} ",e.getMessage().getClass(),e.getMessage());
            assertTrue(e.getMessage().contains("null id"));
        }
        log.info("testing for user not found in the database");
        doReturn(Optional.empty()).when(userRepository).findById(anyInt());
        try {
            ResponseEntity<User> responseEntity = userController.updateUser(1, user1);
        } catch (ResponseStatusException e) {
            log.info("JOERR:{}" , e.getMessage());
            assertTrue(e.getMessage().contains("user with id:1 not found"));
        }

    }















}
