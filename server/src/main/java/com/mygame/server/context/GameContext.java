package com.mygame.server.context;

// src/main/java/com/mygame/server/context/GameContext.java

import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import org.springframework.stereotype.Component;

@Component // Make it a Spring Bean
public class GameContext {

    private Player player;
    private Room currentRoom;
    private boolean gameOver = false;
    
    public GameContext(Player player, Room startRoom) { // Constructor Injection
        this.player = player;
        this.currentRoom = startRoom;
    }

    public GameContext() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
