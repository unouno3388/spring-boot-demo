package com.mygame.server.command;

import java.util.List;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.*;
import com.mygame.server.service.MonsterService;
import com.mygame.server.service.PlayerService;
import com.mygame.server.service.SkillService;
import com.mygame.server.skill.Skill;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

//@Component
public class SkillCommand implements Command {
    private String skillName;
    private final SkillService skillService;
    private final MonsterService monsterService;
    private final PlayerService playerService;
    //@Autowired
    public SkillCommand(String skillName, SkillService skillService, MonsterService monsterService, PlayerService playerService) {
        this.skillName = skillName;
        this.skillService = skillService;
        this.monsterService = monsterService;
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        Room currentRoom = context.getCurrentRoom();
        Monster monster = currentRoom.getMonster();
        StringBuilder output = new StringBuilder();

        if (monster == null || !monsterService.isAlive(monster)) {
            return "這裡沒有可以攻擊的目標！";
        }

        Skill skill = skillService.getSkill(skillName); // 從 SkillService 獲取技能

        if (skill != null) {
            output.append("🔥 你施放了【").append(skill.getName()).append("】！\n");
            skill.use(player, monster); // 使用技能

            // 檢查怪物是否死亡並處理掉落
            if (!monsterService.isAlive(monster)) {
                output.append(monster.getName()).append(" 被你燒成灰燼了！\n");
                playerService.addKill(player);
                List<Item> drops = monsterService.getDrops(monster);
                if (!drops.isEmpty()) {
                    output.append("你獲得了：\n");
                    for (Item item : drops) {
                        playerService.addItemToInventory(player, item);
                        output.append("- ").append(item.getName()).append("\n");
                    }
                }

            } else {
                int monsterDamage = monster.getAttack();
                playerService.takeDamage(player, monsterDamage);
                output.append(monster.getName()).append(" 反擊你，造成 ").append(monsterDamage).append(" 傷害！\n");
            }

            // 狀態更新
            output.append("=== 狀態更新 ===\n");
            output.append("你的 HP： ").append(player.getHp()).append("\n");
            output.append(monster.getName()).append(" HP： ").append(monster.getHp()).append("\n");
            output.append("總傷害： ").append(player.getTotalDamage()).append("，擊殺數： ").append(player.getKillCount()).append("\n");

        } else {
            output.append("你不會這個技能！\n");
        }

        return output.toString();
    }
}