package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import com.mygame.server.service.PlayerService;
import com.mygame.server.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsePotionCommand implements Command {

    private final PlayerService playerService;
    private final RoomService roomService;
    @Autowired
    public UsePotionCommand(PlayerService playerService, RoomService roomService) {
        this.playerService = playerService;
        this.roomService = roomService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Room currentRoom = context.getCurrentRoom();
        if (roomService.hasPotion(currentRoom)) {
            playerService.heal(player, 30);
            roomService.removePotion(currentRoom);
            return "你喝下治療藥水，回復 30 HP！ 你的 HP： " + player.getHp();
        } else {
            return "這裡沒有治療藥水。";
        }
    }
}