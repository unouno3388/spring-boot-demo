package com.mygame.server.factory;

import com.mygame.server.model.Item;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class SimpleItemFactory extends ItemFactory {

    private final Random random = new Random();
    private final List<String> weaponNames = Arrays.asList("鐵劍", "短刀", "法杖");
    private final List<String> armorNames = Arrays.asList("布甲", "皮甲", "鐵甲");
    private final List<String> miscItemNames = Arrays.asList("治療藥水", "金幣");

    @Override
    public Item createItem(String name, String description, int attackBonus, int defenseBonus, int price) {
        return new Item(null, name, description, attackBonus, defenseBonus, price, null);
    }

    // 一個創建隨機武器的範例
    public Item createRandomWeapon() {
        String name = weaponNames.get(random.nextInt(weaponNames.size()));
        String description = "一把普通的" + name;
        int attackBonus = random.nextInt(8) + 2; // 攻擊力在 2 到 9 之間
        return createItem(name, description, attackBonus, 0, random.nextInt(50) + 10);
    }

    // 一個創建隨機防具的範例
    public Item createRandomArmor() {
        String name = armorNames.get(random.nextInt(armorNames.size()));
        String description = "一件" + name;
        int defenseBonus = random.nextInt(6) + 1; // 防禦力在 1 到 6 之間
        return createItem(name, description, 0, defenseBonus, random.nextInt(40) + 5);
    }

    // 一個創建隨機雜物的範例
    public Item createRandomMisc() {
        String name = miscItemNames.get(random.nextInt(miscItemNames.size()));
        String description = name;
        int price = random.nextInt(30) + 1;
        return createItem(name, description, 0, 0, price);
    }
}