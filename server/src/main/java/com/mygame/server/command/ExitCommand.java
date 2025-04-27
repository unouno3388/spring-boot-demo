package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.*;
public class ExitCommand implements Command {
    @Override
    public String execute(Player player, GameContext context) {
        StringBuilder output = new StringBuilder();
        //System.out.println("👋 感謝遊玩！再會，勇者！");
        output.append("👋 感謝遊玩！再會，勇者！").append("\n");
        // 在這裡設置一個標誌，讓遊戲迴圈知道要結束
        // 或者你可以直接使用 System.exit(0); 來終止程式
        // 我會建議使用一個標誌，因為這樣可以更優雅地處理遊戲結束的邏輯
        context.setGameOver(true); // 假設 GameContext 有 setGameOver 方法
        return output.toString();
    }
}
