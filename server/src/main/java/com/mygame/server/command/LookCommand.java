package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import com.mygame.server.model.Monster;
import com.mygame.server.service.MonsterService;
import com.mygame.server.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LookCommand implements Command {

    private final RoomService roomService;
    private final MonsterService monsterService;
    @Autowired
    public LookCommand(RoomService roomService, MonsterService monsterService) {
        this.roomService = roomService;
        this.monsterService = monsterService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Room currentRoom = context.getCurrentRoom();
        StringBuilder output = new StringBuilder();

        output.append("【房間】： ").append(currentRoom.getName()).append("\n");
        output.append("【描述】： ").append(currentRoom.getDescription()).append("\n");

        Monster monster = currentRoom.getMonster();
        if (monster != null && monsterService.isAlive(monster)) {
            output.append("【怪物】： ").append(monster.getName()).append(" (HP: ").append(monster.getHp()).append(")\n");
        }

        if (roomService.hasPotion(currentRoom)) {
            output.append("【道具】： 治療藥水\n");
        }

        String exits = roomService.getExitString(currentRoom);
        if (!exits.isEmpty()) {
            output.append("【出口】： ").append(exits).append("\n");
        } else {
            output.append("這個房間沒有明顯的出口。\n");
        }

        return output.toString();
    }
}