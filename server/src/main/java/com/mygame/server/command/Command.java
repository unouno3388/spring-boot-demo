package com.mygame.server.command;
import com.mygame.server.model.Player;
import com.mygame.server.context.*;
public interface Command {
    String execute(Player player, GameContext context);
}
