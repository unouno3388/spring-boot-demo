package com.mygame.server.context;

// src/main/java/com/mygame/server/context/GameContext.java

import com.mygame.server.model.*;
import com.mygame.server.service.*;
import org.springframework.stereotype.Component;

//@Component // Make it a Spring Bean
public class GameContext {

    private final PlayerService playerService;
    private final RoomService roomService;
    
    private Player player;
    private Room currentRoom;
    private boolean gameOver = false;

    // 改為依賴注入 PlayerService 和 RoomService
    public GameContext(PlayerService playerService, RoomService roomService) {
        this.playerService = playerService;
        this.roomService = roomService;
    }

    // 初始化遊戲（創建玩家和初始房間）
    public void initializeGame(String playerName) {
        this.player = playerService.createNewPlayer(
            playerName, 
            100,  // baseHp
            10,   // baseAttack
            5     // baseDefense
        );
        
        this.currentRoom = roomService.createNewRoom(
            "初始房間", 
            "這是一個充滿冒險氣息的起點。"
        );
        
        // 設置玩家當前房間
        playerService.setCurrentRoomId(player, currentRoom.getId());
    }

    public Player getPlayer() {
        if (player == null) {
            throw new IllegalStateException("玩家未初始化");
        }
        return player;
    }



    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getCurrentRoom() {
        if (currentRoom == null) {
            throw new IllegalStateException("當前房間未初始化");
        }
        return currentRoom;
    }
    public void moveToRoom(Room newRoom) {
        this.currentRoom = newRoom;
        playerService.setCurrentRoomId(player, newRoom.getId());
    }
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void endGame() {
        this.gameOver = true;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
