package com.mygame.server.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private Long currentRoomId; //  新增：玩家目前所在房間的 ID

    public boolean isAlive() {
        return hp > 0;
    }
    
    // 你可能還需要添加無參數的建構子 (no-argument constructor)
    public Player() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return hp == player.hp && baseAttack == player.baseAttack && attack == player.attack && defense == player.defense && killCount == player.killCount && totalDamage == player.totalDamage && Objects.equals(id, player.id) && Objects.equals(name, player.name) && Objects.equals(equipment, player.equipment) && Objects.equals(inventory, player.inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hp, baseAttack, attack, defense, killCount, totalDamage, equipment, inventory);
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
               ", equipment=" + equipment +
               ", inventory=" + inventory +
               '}';
    }

}