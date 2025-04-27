package com.mygame.server.service;

import com.mygame.server.factory.ItemFactory;
import com.mygame.server.model.Item;
import com.mygame.server.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemFactory itemFactory;

    @Autowired
    public ItemService(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }

    public Item createNewItem(String name, String description, int attackBonus, int defenseBonus, int price) {
        return itemFactory.createItem(name, description, attackBonus, defenseBonus, price);
    }

    public void addItemToRoom(Item item, Room room) {
        item.setRoom(room);
        // 如果 Room 類別中也有管理物品的列表，你可能也需要將 item 添加到 room 的列表中
        // 例如： room.getItems().add(item);
    }

    // 你可以根據需要添加更多與物品相關的業務邏輯
}