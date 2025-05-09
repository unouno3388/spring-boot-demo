package com.mygame.server.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int hp;
    private int baseAttack;
    private int attack;
    private int defense;
    private int killCount = 0;
    private int totalDamage = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_id")
    private List<Item> equipment = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "player_inventory",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> inventory = new ArrayList<>();

    private Long currentRoomId;

    public Player() {
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // 添加 getEquipmentString 方法供前端使用
    public String getEquipmentString() {
        if (equipment == null || equipment.isEmpty()) {
            return "無裝備";
        }
        return equipment.stream()
                .map(item -> item != null ? item.getName() : "未知物品")
                .collect(Collectors.joining(", "));
    }

    // 添加 getInventoryString 方法供前端使用
    public String getInventoryString() {
        if (inventory == null || inventory.isEmpty()) {
            return "無物品";
        }
        return inventory.stream()
                .map(item -> item != null ? item.getName() : "未知物品")
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return hp == player.hp &&
               baseAttack == player.baseAttack &&
               attack == player.attack &&
               defense == player.defense &&
               killCount == player.killCount &&
               totalDamage == player.totalDamage &&
               Objects.equals(id, player.id) &&
               Objects.equals(name, player.name) &&
               Objects.equals(currentRoomId, player.currentRoomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hp, baseAttack, attack, defense, killCount, totalDamage, currentRoomId);
    }

    @Override
    public String toString() {
        return "Player{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", hp=" + hp +
               ", baseAttack=" + baseAttack +
               ", attack=" + attack +
               ", defense=" + defense +
               ", killCount=" + killCount +
               ", totalDamage=" + totalDamage +
               ", equipmentCount=" + (equipment != null ? equipment.size() : 0) +
               ", inventoryCount=" + (inventory != null ? inventory.size() : 0) +
               ", currentRoomId=" + currentRoomId +
               '}';
    }
}