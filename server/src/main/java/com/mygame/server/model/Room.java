
package com.mygame.server.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Monster monster;

    private boolean hasPotion;

    @ManyToMany
    @JoinTable(
            name = "room_exits",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "exit_room_id")
    )
    private Map<String, Room> exits = new HashMap<>();

    // JPA 要求有預設的無參數建構子
    public Room() {
    }

    // 手動實現 getter 和 setter
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public boolean isHasPotion() {
        return hasPotion;
    }

    public void setHasPotion(boolean hasPotion) {
        this.hasPotion = hasPotion;
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public void setExits(Map<String, Room> exits) {
        this.exits = exits;
    }

    public String getExitString() {
        return String.join(", ", exits.keySet());
    }

    @Override
    public String toString() {
        return "Room{id=" + id + ", name='" + name + "', description='" + description + 
               ", hasPotion=" + hasPotion + ", exitsCount=" + (exits != null ? exits.size() : 0) + "}";
    }
}
/*
package com.mygame.server.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Monster monster;

    private boolean hasPotion;

    @ManyToMany
    @JoinTable(
            name = "room_exits",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "exit_room_id")
    )
    private Map<String, Room> exits = new HashMap<>();

    public String getExitString() {
        return String.join(", ", exits.keySet());
    }

    // JPA 要求有預設的無參數建構子
    public Room() {
    }


}*/