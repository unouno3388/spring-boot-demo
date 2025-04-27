package com.mygame.server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
//import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int attackBonus;
    private int defenseBonus;
    private int price;

    @ManyToOne
    @JoinColumn(name = "room_id") // 指定外鍵欄位名稱
    private Room room;
    
    public String toString() {
        return "Item{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", attackBonus=" + attackBonus +
               ", defenseBonus=" + defenseBonus +
               ", price=" + price +
               ", room=" + (room != null ? room.getId() : null) + // 避免循環引用導致 StackOverflowError
               '}';
    }
    /* 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return attackBonus == item.attackBonus && defenseBonus == item.defenseBonus && price == item.price && Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(room, item.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, attackBonus, defenseBonus, price, room);
    }

    @Override
    
     */
}