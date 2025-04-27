package com.mygame.server.factory;

import com.mygame.server.model.Monster;

import java.util.Random;

public abstract class MonsterFactory {
    protected Random random = new Random();

    public abstract Monster createMonster(String name, int hp, int attack);

    public Monster createRandomMonster() {
        // 可以在這裡定義一些通用的隨機怪物創建邏輯
        return null; // 具體實作在子類別中
    }
}