package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.model.Item;
import com.mygame.server.service.PlayerService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentCommand implements Command {

    private final PlayerService playerService;
    @Autowired
    public EquipmentCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        List<Item> equipment = player.getEquipment();
        StringBuilder output = new StringBuilder();

        if (equipment.isEmpty()) {
            return player.getName() + " 沒有裝備任何物品。";
        } else {
            output.append(player.getName() + " 裝備了：\n");
            for (Item item : equipment) {
                output.append("- ").append(item.getName()).append(": ").append(item.getDescription()).append("\n");
            }
            return output.toString();
        }
    }
}