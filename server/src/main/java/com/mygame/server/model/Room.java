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


}