package com.mygame.server.service;

import com.mygame.server.command.Command;
import com.mygame.server.command.CommandParser;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/* 
@Service
public class CommandService {

    private final CommandParser commandParser;

    @Autowired
    public CommandService(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public void executeCommand(String input, Player player, GameContext context) {
        Command command = commandParser.parse(input);
        if (command != null) {
            command.execute(player, context);
        } else {
            System.out.println("未知的命令。請輸入 'help' 查看可用命令。");
        }
    }
}*/