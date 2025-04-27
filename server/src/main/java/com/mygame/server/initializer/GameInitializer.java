package com.mygame.server.initializer;

import com.mygame.server.context.GameContext;
//import com.mygame.server.factory.ItemFactory;
import com.mygame.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mygame.server.service.ItemService;
import com.mygame.server.service.MonsterService;
import com.mygame.server.service.PlayerService;
import com.mygame.server.service.RoomService;

@Component
public class GameInitializer {

    private final RoomService roomService;
    private final PlayerService playerService;
    private final ItemService itemService;
    private final MonsterService monsterService;
    //private final ItemFactory itemFactory; // 注入 ItemFactory

    @Autowired
    public GameInitializer(RoomService roomService, PlayerService playerService, ItemService itemService, MonsterService monsterService) {
        this.roomService = roomService;
        this.playerService = playerService;
        this.itemService = itemService;
        this.monsterService = monsterService;
        //this.itemFactory = itemFactory;
    }

    public GameContext init() {
        Room forest = roomService.createNewRoom("森林入口", "你站在茂密森林的邊緣");
        Room temple = roomService.createNewRoom("神殿大廳", "光線從破碎的石窗灑落");

        Monster goblin = monsterService.createNewMonster("哥布林", 30, 8);
        Monster warrior = monsterService.createNewMonster("亡靈戰士", 50, 12);

        roomService.setMonster(forest, goblin);
        roomService.setMonster(temple, warrior);
        roomService.setHasPotion(forest, true);
        roomService.setHasPotion(temple, false);

        roomService.setExit(forest, "north", temple);
        roomService.setExit(temple, "south", forest);

        Player player = playerService.createNewPlayer("勇者", 100, 15, 5);

        // 使用 ItemService (它會使用 ItemFactory) 創建物品
        Item sword = itemService.createNewItem("鐵劍", "一把鋒利的鐵劍", 5, 0, 0);
        Item shield = itemService.createNewItem("木盾", "一面堅固的木盾", 0, 5, 0);
        Item goblinTooth = itemService.createNewItem("哥布林之牙", "可以用來交易", 0, 0, 5);
        Item tatteredArmor = itemService.createNewItem("破爛布甲", "稍微提供保護", 0, 1, 10);
        Item rustySword = itemService.createNewItem("生鏽的劍", "一把生鏽的劍", 2, 0, 15);

        playerService.addItemToInventory(player, sword);
        playerService.addItemToInventory(player, shield);

        monsterService.addDrop(goblin, goblinTooth);
        monsterService.addDrop(goblin, tatteredArmor);

        monsterService.addDrop(warrior, rustySword);

        return new GameContext(player, forest);
    }
}