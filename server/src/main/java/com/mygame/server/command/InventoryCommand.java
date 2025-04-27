package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryCommand implements Command {

    private final PlayerService playerService;
    @Autowired
    public InventoryCommand(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        return playerService.displayInventory(player);
    }
}