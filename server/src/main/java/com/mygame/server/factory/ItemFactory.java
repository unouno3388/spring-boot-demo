package com.mygame.server.factory;

import com.mygame.server.model.Item;

import java.util.Random;

public abstract class ItemFactory {
    protected Random random = new Random();

    public abstract Item createItem(String name, String description, int attackBonus, int defenseBonus, int price);

    // 可以添加創建隨機屬性物品的通用方法，如果需要
    // public abstract Item createRandomItem();
}
