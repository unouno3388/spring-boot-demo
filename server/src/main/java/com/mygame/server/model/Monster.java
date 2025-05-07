package com.mygame.server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int hp;
    private int attack;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "monster_drops",
            joinColumns = @JoinColumn(name = "monster_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> drops = new ArrayList<>();

    // 無參數建構子
    public Monster() {
    }

    // 全參數建構子
    public Monster(Long id, String name, int hp, int attack, List<Item> drops) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.drops = drops != null ? drops : new ArrayList<>();
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public List<Item> getDrops() {
        return drops;
    }

    public void setDrops(List<Item> drops) {
        this.drops = drops;
    }

    @Override
    public String toString() {
        return "Monster{id=" + id + ", name='" + name + "', hp=" + hp + 
               ", attack=" + attack + ", dropsCount=" + (drops != null ? drops.size() : 0) + "}";
    }
}
/*
package com.mygame.server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int hp;
    private int attack;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "monster_drops",
            joinColumns = @JoinColumn(name = "monster_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> drops = new ArrayList<>(); // 掉落物品列表


}
*/