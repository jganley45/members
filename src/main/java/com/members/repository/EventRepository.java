package com.members.repository;

import com.members.model.Event;
import com.members.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

  Optional<Event> findByEventName(String eventName);
  Optional<Event> findById(Integer id);
  Optional<Event> findByDescription(String description);
  Optional<Event> findByLocation(String location);


}
