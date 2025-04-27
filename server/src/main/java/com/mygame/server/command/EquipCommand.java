package com.mygame.server.command;

import com.mygame.server.context.GameContext;
import com.mygame.server.model.Item;
import com.mygame.server.model.Player;
import com.mygame.server.service.PlayerService;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

//@Component
public class EquipCommand implements Command {

    private final String itemName;
    private final PlayerService playerService;
    //@Autowired
    public EquipCommand(String itemName, PlayerService playerService) {
        this.itemName = itemName;
        this.playerService = playerService;
    }

    @Override
    public String execute(Player player, GameContext context) {
        // 1. 在玩家的背包中尋找要裝備的物品
        Item itemToEquip = playerService.findItemInInventory(player, itemName);

        // 2. 如果找到了物品，則進行裝備
        if (itemToEquip != null) {
            return playerService.equipItem(player, itemToEquip); // 調用 PlayerService 的 equipItem 方法
        } else {
            return "你的背包中沒有 " + itemName + "。";
        }
    }
}