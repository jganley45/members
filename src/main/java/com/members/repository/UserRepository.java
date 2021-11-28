package com.members.repository;

import com.members.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findById(String name);

  Optional<User> findByLoginId(String loginId);
  Optional<User> findByEmail(String email);


  List<User> findAllByLoginId(String loginId);


}
