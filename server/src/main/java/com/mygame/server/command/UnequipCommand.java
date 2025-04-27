package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Item;
import com.mygame.server.model.Player;
import com.mygame.server.service.PlayerService;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

//@Component
public class UnequipCommand implements Command {

    private final String itemName;
    private final PlayerService playerService;
    //@Autowired
    public UnequipCommand(String itemName, PlayerService playerService) {
        this.itemName = itemName;
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Item itemToUnequip = playerService.findEquippedItem(player, itemName);
        if (itemToUnequip != null) {
            playerService.unequipItem(player, itemToUnequip);
            return "你卸下了 " + itemName + "。";
        } else {
            return "你沒有裝備 " + itemName + "。";
        }
    }
}