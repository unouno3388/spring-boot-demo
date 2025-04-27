package com.mygame.server.skill;

import com.mygame.server.model.Monster;
import com.mygame.server.model.Player;
import com.mygame.server.service.MonsterService;
import com.mygame.server.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FireballSkill implements Skill {

    private final MonsterService monsterService;
    private final PlayerService playerService;

    @Autowired
    public FireballSkill(MonsterService monsterService, PlayerService playerService) {
        this.monsterService = monsterService;
        this.playerService = playerService;
    }

    public String getName() {
        return "火球術";
    }

    public void use(Player player, Monster monster) {
        int fireballDamage = 40; // 火球術傷害
        monsterService.takeDamage(monster, fireballDamage);
        playerService.addDamage(player, fireballDamage); // 累計玩家總傷害
    }
}