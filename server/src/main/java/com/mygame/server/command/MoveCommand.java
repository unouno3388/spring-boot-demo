package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import com.mygame.server.model.Monster;
import com.mygame.server.service.RoomService;
import com.mygame.server.service.MonsterService;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

//@Component
public class MoveCommand implements Command {

    private final String destination;
    private final RoomService roomService;
    private final MonsterService monsterService;
    //@Autowired
    public MoveCommand(String destination, RoomService roomService, MonsterService monsterService) {
        this.destination = destination.trim();
        this.roomService = roomService;
        this.monsterService = monsterService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Room currentRoom = context.getCurrentRoom();
        Room nextRoom = roomService.getExit(currentRoom, destination);
        StringBuilder output = new StringBuilder();

        if (nextRoom != null) {
            context.setCurrentRoom(nextRoom);
            output.append("你移動到了： ").append(nextRoom.getName()).append("\n");

            Monster monster = nextRoom.getMonster();
            if (monster != null && monsterService.isAlive(monster)) {
                output.append("你看到： ").append(monster.getName()).append(" (HP: ").append(monster.getHp()).append(")\n");
            }

            String exits = roomService.getExitString(nextRoom);
            output.append("可用方向： ").append(exits).append("\n");

        } else {
            output.append("無法往 ").append(destination).append(" 移動。\n");
        }

        return output.toString();
    }
}