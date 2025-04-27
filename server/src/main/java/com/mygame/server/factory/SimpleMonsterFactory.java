package com.mygame.server.factory;

import com.mygame.server.model.Monster;
import org.springframework.stereotype.Component; // 如果你使用 Spring

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component // 如果你使用 Spring，將其標註為一個 Bean
public class SimpleMonsterFactory extends MonsterFactory {

    private final Random random = new Random();
    private final List<String> monsterNames = Arrays.asList("哥布林", "史萊姆", "骷髏", "蝙蝠");

    @Override
    public Monster createMonster(String name, int hp, int attack) {
        return new Monster(null, name, hp, attack, null); // 注意 id 通常由資料庫生成，drops 初始為 null 或空列表
    }

    @Override
    public Monster createRandomMonster() {
        String randomName = monsterNames.get(random.nextInt(monsterNames.size()));
        int randomHp = random.nextInt(50) + 50; // HP 在 50 到 100 之間
        int randomAttack = random.nextInt(10) + 5; // 攻擊力在 5 到 15 之間
        return createMonster(randomName, randomHp, randomAttack);
    }
}