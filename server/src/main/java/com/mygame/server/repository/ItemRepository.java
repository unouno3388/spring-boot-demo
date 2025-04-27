/* 
package com.mygame.server.repository;

 import com.mygame.server.model.Item;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 import java.util.List;

 @Repository
 public interface ItemRepository extends JpaRepository<Item, Long> {
     // 找到與特定玩家的庫存相關聯的物品
     List<Item> findByPlayers_Id(Long playerId);

     // 找到與特定玩家的裝備相關聯的物品
     List<Item> findByEquipment_Player_Id(Long playerId);

     // 你可以添加其他自定義查詢方法
 }
*/