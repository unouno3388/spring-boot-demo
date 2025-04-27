package com.mygame.server.engine;

/* 

import com.mygame.server.context.GameContext;
import com.mygame.server.service.CommandService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class GameEngine {

    private final GameContext context;
    private final CommandService commandService;
    private final Scanner scanner = new Scanner(System.in);
    @Autowired
    public GameEngine(GameContext context, CommandService commandService) { // Constructor Injection
        this.context = context;
        this.commandService = commandService;
    }

    public void start() {
        // 讓玩家輸入名稱
        System.out.print("請輸入你的名字：");
        String playerName = scanner.nextLine();
        context.getPlayer().setName(playerName); // 設定玩家名稱

        System.out.println("👾 [ 遊戲開始！歡迎 " + context.getPlayer().getName() + " 勇者！ ]");
        System.out.println("你目前在：" + context.getCurrentRoom().getName());
        System.out.println(context.getCurrentRoom().getDescription());

        while (context.getPlayer().isAlive() && !context.isGameOver()) {
            System.out.print("> ");
            String input = scanner.nextLine();
            commandService.executeCommand(input, context.getPlayer(), context);
        }
        if (!context.getPlayer().isAlive()) {
            System.out.println("你已死亡，遊戲結束。");
        }
        scanner.close();
    }
}
*/