package com.mygame.server.repository;

 import com.mygame.server.model.Room;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 import java.util.Optional;

 @Repository
 public interface RoomRepository extends JpaRepository<Room, Long> {
     Optional<Room> findByName(String name);
 }