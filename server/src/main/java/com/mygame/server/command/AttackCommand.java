package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.*;
import com.mygame.server.service.MonsterService;
import com.mygame.server.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class AttackCommand implements Command {

    private final MonsterService monsterService;
    private final PlayerService playerService;
    @Autowired
    public AttackCommand(MonsterService monsterService, PlayerService playerService) {
        this.monsterService = monsterService;
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Room currentRoom = context.getCurrentRoom();
        Monster monster = currentRoom.getMonster();
        StringBuilder output = new StringBuilder(); // 使用 StringBuilder 來建構輸出

        if (monster == null || !monsterService.isAlive(monster)) {
            return "這裡沒有可以攻擊的目標！";
        }

        // 玩家攻擊怪物
        int playerDamage = player.getAttack();
        monsterService.takeDamage(monster, playerDamage);
        playerService.addDamage(player, playerDamage);
        output.append("你對 ").append(monster.getName()).append(" 造成了 ").append(playerDamage).append(" 傷害！\n");

        // 怪物反擊（如果還活著）
        if (monsterService.isAlive(monster)) {
            int monsterDamage = monster.getAttack();
            playerService.takeDamage(player, monsterDamage);
            output.append(monster.getName()).append(" 反擊你，造成 ").append(monsterDamage).append(" 傷害！\n");
        } else {
            output.append(monster.getName()).append(" 被你擊敗了！\n");
            playerService.addKill(player);

            // Add monster's drops to player's inventory
            List<Item> drops = monsterService.getDrops(monster);
            if (!drops.isEmpty()) {
                output.append("你獲得了：\n");
                for (Item item : drops) {
                    playerService.addItemToInventory(player, item);
                    output.append("- ").append(item.getName()).append("\n");
                }
            }
        }

        // 狀態更新
        output.append("=== 狀態更新 ===\n");
        output.append("你的 HP： ").append(player.getHp()).append("\n");
        output.append(monster.getName()).append(" HP： ").append(monster.getHp()).append("\n");
        output.append("總傷害： ").append(player.getTotalDamage()).append("，擊殺數： ").append(player.getKillCount()).append("\n");

        return output.toString(); // 返回建構好的字串
    }
}