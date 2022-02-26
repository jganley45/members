package com.members.controller;

import com.members.model.User;
import com.members.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequestMapping(path = {"/"})
@RestController
public class UserController {
    @Autowired
    UserRepository  userRepository;


    @GetMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
           path = {"user/users"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<User>> getUsers() {
        log.info("Here in get-users");
       List<User> users = userRepository.findAll();
       return ResponseEntity.ok(users);
    }


    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE,
          path = "user")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (Objects.isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null user");
        }
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
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("no user found"));
        }
        userRepository.delete(user.get());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "user/{id}", consumes = "application/json")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        log.info("id1: {}", id);
        if (Objects.isNull(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null user");
        }
        if (Objects.isNull(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "null id");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            // https://www.baeldung.com/spring-response-status-exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("user with id:%d not found", id));
        }
        log.info("user: {}", user);
        if (Objects.nonNull(user.getEmail())) {
            userOptional.get().setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getName())) {
            userOptional.get().setName(user.getName());
        }
        if (Objects.nonNull(user.getLoginId())) {
            userOptional.get().setLoginId(user.getLoginId());
        }
        userRepository.saveAndFlush(userOptional.get());
        return ResponseEntity.ok(userOptional.get());
    }


}
