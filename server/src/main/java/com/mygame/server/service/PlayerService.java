package com.mygame.server.service;

import com.mygame.server.model.Item;
import com.mygame.server.model.Player;
import com.mygame.server.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public Player createNewPlayer(String name, int baseHp, int baseAttack, int baseDefense) {
        Player player = new Player();
        player.setName(name);
        player.setHp(baseHp);
        player.setBaseAttack(baseAttack);
        player.setAttack(baseAttack);
        player.setDefense(baseDefense);
        return playerRepository.save(player);
    }

    @Transactional
    public void setCurrentRoomId(Player player, Long ID) {
        player.setCurrentRoomId(ID);
        playerRepository.save(player);
    }

    public Long getCurrentRoomId(Player player) {
        return player.getCurrentRoomId();
    }

    @Transactional
    public void addDamage(Player player, int damage) {
        player.setTotalDamage(player.getTotalDamage() + damage);
        playerRepository.save(player);
    }

    @Transactional
    public void takeDamage(Player player, int damage) {
        player.setHp(player.getHp() - damage);
        if (player.getHp() < 0) {
            player.setHp(0);
        }
        playerRepository.save(player);
    }

    @Transactional
    public void heal(Player player, int amount) {
        player.setHp(player.getHp() + amount);
        playerRepository.save(player);
    }

    @Transactional
    public void addKill(Player player) {
        player.setKillCount(player.getKillCount() + 1);
        playerRepository.save(player);
    }

    @Transactional
    public String equipItem(Player player, Item item) {
        List<Item> inventory = player.getInventory();
        List<Item> equipment = player.getEquipment();
        StringBuilder output = new StringBuilder();

        if (inventory.contains(item)) {
            equipment.add(item);
            inventory.remove(item);
            player.setAttack(player.getAttack() + item.getAttackBonus());
            player.setDefense(player.getDefense() + item.getDefenseBonus());
            output.append(player.getName()).append(" 裝備了 ").append(item.getName()).append("！");
            playerRepository.save(player);
        } else {
            output.append(player.getName()).append(" 的背包中沒有 ").append(item.getName()).append("。");
        }
        return output.toString();
    }

    @Transactional
    public String unequipItem(Player player, Item item) {
        List<Item> inventory = player.getInventory();
        List<Item> equipment = player.getEquipment();
        StringBuilder output = new StringBuilder();

        if (equipment.contains(item)) {
            equipment.remove(item);
            inventory.add(item);
            player.setAttack(player.getAttack() - item.getAttackBonus());
            player.setDefense(player.getDefense() - item.getDefenseBonus());
            output.append(player.getName()).append(" 卸下了 ").append(item.getName()).append("。");
            playerRepository.save(player);
        } else {
            output.append(player.getName()).append(" 沒有裝備 ").append(item.getName()).append("。");
        }
        return output.toString();
    }

    @Transactional
    public String addItemToInventory(Player player, Item item) {
        player.getInventory().add(item);
        playerRepository.save(player);
        return player.getName() + " 獲得了 " + item.getName() + "！";
    }

    @Transactional
    public Item findItemInInventory(Player player, String itemName) {
        for (Item item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    @Transactional
    public Item findEquippedItem(Player player, String itemName) {
        for (Item item : player.getEquipment()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    @Transactional
    public Player findByName(String name) {
        System.out.println("PlayerService.findByName() 查詢玩家: " + name);
        Optional<Player> playerOpt = playerRepository.findByName(name);
        Player player = playerOpt.orElse(null);
        if (player != null) {
            // 主動加載 equipment 和 inventory
            player.getEquipment().size();
            player.getInventory().size();
            System.out.println("PlayerService.findByName() 查詢結果: " + player.toString());
        } else {
            System.out.println("PlayerService.findByName() 查詢結果: null");
        }
        return player;
    }

    @Transactional
    public String displayEquipment(Player player) {
        StringBuilder output = new StringBuilder();
        if (player.getEquipment().isEmpty()) {
            output.append(player.getName()).append(" 沒有裝備任何物品。");
        } else {
            output.append(player.getName()).append(" 裝備了：\n");
            for (Item item : player.getEquipment()) {
                output.append("- ").append(item.getName()).append(": ").append(item.getDescription()).append("\n");
            }
        }
        return output.toString();
    }

    @Transactional
    public String displayInventory(Player player) {
        StringBuilder output = new StringBuilder();
        if (player.getInventory().isEmpty()) {
            output.append(player.getName()).append(" 的背包是空的。");
        } else {
            output.append(player.getName()).append(" 的背包裡有：\n");
            for (Item item : player.getInventory()) {
                output.append("- ").append(item.getName()).append(": ").append(item.getDescription()).append(" (價格: ").append(item.getPrice()).append(")\n");
            }
        }
        return output.toString();
    }

    public String displayStats(Player player) {
        StringBuilder output = new StringBuilder();
        output.append("=== 玩家狀態 ===\n");
        output.append("名稱: ").append(player.getName()).append("\n");
        output.append("HP: ").append(player.getHp()).append("\n");
        output.append("攻擊力: ").append(player.getAttack()).append("\n");
        output.append("防禦力: ").append(player.getDefense()).append("\n");
        output.append("總傷害: ").append(player.getTotalDamage()).append("\n");
        output.append("擊殺數: ").append(player.getKillCount()).append("\n");
        return output.toString();
    }
}