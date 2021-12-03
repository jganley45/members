package com.members.controller;

import com.members.model.User;
import com.members.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequestMapping(path = {"/user"})
@RestController
public class UserController {
    @Autowired
    UserRepository  userRepository;


    @GetMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
           path = {"get-users"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getUserSession() {
        log.info("Here in get-users");
       List<User> users = userRepository.findAll();
       return ResponseEntity.ok(users);
    }


    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE,
          path = "user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (Objects.isNull(user)) {
            throw new RuntimeException("null user");
        }
        log.info("user: {}", user);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "user/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        log.info("id1: {}", id);
        if (Objects.isNull(id)) {
            throw new RuntimeException("null id");
        }
        log.info("id2: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException(String.format("user with %d not found", id));
        }
        userRepository.delete(user.get());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }




}
