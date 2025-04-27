package com.mygame.server.service;

import com.mygame.server.model.Item;
import com.mygame.server.model.Monster;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonsterService {

    public Monster createNewMonster(String name, int hp, int attack) {
        Monster monster = new Monster();
        monster.setName(name);
        monster.setHp(hp);
        monster.setAttack(attack);
        return monster;
    }

    public void takeDamage(Monster monster, int damage) {
        monster.setHp(monster.getHp() - damage);
    }

    public boolean isAlive(Monster monster) {
        return monster.getHp() > 0;
    }

    public void addDrop(Monster monster, Item item) {
        monster.getDrops().add(item);
    }

    public List<Item> getDrops(Monster monster) {
        return monster.getDrops();
    }

    // 你可以根據需要添加更多與怪物相關的業務邏輯，例如：
    // - 處理怪物的攻擊行為
    // - 怪物的特殊技能
    // - 怪物的死亡邏輯（掉落物品、經驗值等）
    // - 根據等級或區域創建不同屬性的怪物
}
