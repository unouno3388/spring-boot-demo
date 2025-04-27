package com.mygame.server.service;

 import com.mygame.server.model.Item;
 import com.mygame.server.model.Monster;
 import com.mygame.server.model.Room;
 import com.mygame.server.repository.RoomRepository; // 確保注入 RoomRepository
 import org.springframework.stereotype.Service;
 import org.springframework.beans.factory.annotation.Autowired; // 引入 Autowired
 //import java.util.Map;

 @Service
 public class RoomService {

     private final RoomRepository roomRepository;

     @Autowired
     public RoomService(RoomRepository roomRepository) {
         this.roomRepository = roomRepository;
     }

     public Room createNewRoom(String name, String description) {
         Room room = new Room();
         room.setName(name);
         room.setDescription(description);
         return roomRepository.save(room); // 使用 repository.save()
     }

     public String getExitString(Room room) {
         return String.join(", ", room.getExits().keySet());
     }

     public void setExit(Room room, String direction, Room exitRoom) {
         room.getExits().put(direction, exitRoom);
         roomRepository.save(room);
     }

     public Room getExit(Room room, String direction) {
         return room.getExits().get(direction);
     }

     public void setMonster(Room room, Monster monster) {
         room.setMonster(monster);
         roomRepository.save(room);
     }

     public void removePotion(Room room) {
         room.setHasPotion(false);
         roomRepository.save(room);
     }

     public boolean hasPotion(Room room) {
         return room.isHasPotion();
     }

     public void setHasPotion(Room room, boolean hasPotion) {
         room.setHasPotion(hasPotion);
         roomRepository.save(room);
     }

     public void addItemToRoom(Room room, Item item) {
         //  room.getItems().add(item); // 假設 Room 有 getItems() 方法
         //  roomRepository.save(room);
     }
     // 你可以根據需要添加更多與房間相關的業務邏輯
 }