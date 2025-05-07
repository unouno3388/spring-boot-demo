package com.mygame.server;
import com.mygame.server.model.*;
import com.mygame.server.repository.PlayerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/* 
@DataJpaTest
@Transactional // 使用 @Transactional 讓每個測試方法結束後回滾
public class EntityTests {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void testSaveAndRetrievePlayer() {
        Player player = new Player();
        player.setName("Test Player");
        player.setHp(100);
        player.setBaseAttack(10);
        player.setAttack(10);
        player.setDefense(5);

        entityManager.persistAndFlush(player); // Save and immediately persist

        Player retrievedPlayer = entityManager.find(Player.class, player.getId());
        assertThat(retrievedPlayer).isNotNull();
        assertThat(retrievedPlayer.getName()).isEqualTo("Test Player");
        assertThat(retrievedPlayer.getHp()).isEqualTo(100);
    }

    @Test
    public void testSaveMultiplePlayers() {
        // 創建第一位玩家
        Player player1 = new Player();
        player1.setName("Player One");
        player1.setHp(100);
        entityManager.persist(player1);

        // 創建第二位玩家
        Player player2 = new Player();
        player2.setName("Player Two");
        player2.setHp(120);
        entityManager.persist(player2);

        // 創建第三位玩家
        Player player3 = new Player();
        player3.setName("Player Three");
        player3.setHp(80);
        entityManager.persist(player3);

        // 強制將所有變更寫入資料庫
        entityManager.flush();

        // 查詢資料庫中的所有玩家
        List<Player> allPlayers = playerRepository.findAll();
        System.out.println("\n"+"Players in database: " + allPlayers+"\n");

        // 驗證是否儲存了三位玩家
        assertThat(allPlayers).hasSize(3);

        // 驗證玩家的姓名是否正確
        assertThat(allPlayers)
                .extracting(Player::getName)
                .containsExactlyInAnyOrder("Player One", "Player Two", "Player Three");

        // 你可以進一步驗證其他屬性，例如 HP
        assertThat(allPlayers)
                .extracting(Player::getHp)
                .containsExactlyInAnyOrder(100, 120, 80);
    }

    @Test
    public void testSaveAndRetrieveRoom() {
        Room room = new Room();
        room.setName("Test Room");
        room.setDescription("Test Description");

        entityManager.persistAndFlush(room);

        Room retrievedRoom = entityManager.find(Room.class, room.getId());
        assertThat(retrievedRoom).isNotNull();
        assertThat(retrievedRoom.getName()).isEqualTo("Test Room");
        assertThat(retrievedRoom.getDescription()).isEqualTo("Test Description");
    }

    @Test
    public void testRoomExitsRelationship() {
        Room room1 = new Room();
        room1.setName("Room 1");
        room1.setDescription("Description 1");
        entityManager.persistAndFlush(room1);

        Room room2 = new Room();
        room2.setName("Room 2");
        room2.setDescription("Description 2");
        entityManager.persistAndFlush(room2);

        room1.getExits().put("north", room2);
        entityManager.persistAndFlush(room1);

        Room retrievedRoom1 = entityManager.find(Room.class, room1.getId());
        assertThat(retrievedRoom1).isNotNull();
        assertThat(retrievedRoom1.getExits().get("north").getName()).isEqualTo("Room 2");
    }

    @Test
    public void testSaveAndRetrieveItem() {
        Item item = new Item(null, "Sword", "A basic sword", 5, 0, 10, null);

        entityManager.persistAndFlush(item);

        Item retrievedItem = entityManager.find(Item.class, item.getId());
        assertThat(retrievedItem).isNotNull();
        assertThat(retrievedItem.getName()).isEqualTo("Sword");
        assertThat(retrievedItem.getAttackBonus()).isEqualTo(5);
    }

    @Test
    public void testSaveAndRetrieveMonster() {
        Monster monster = new Monster(null, "Goblin", 50, 10, null);

        entityManager.persistAndFlush(monster);

        Monster retrievedMonster = entityManager.find(Monster.class, monster.getId());
        assertThat(retrievedMonster).isNotNull();
        assertThat(retrievedMonster.getName()).isEqualTo("Goblin");
        assertThat(retrievedMonster.getHp()).isEqualTo(50);
        assertThat(retrievedMonster.getAttack()).isEqualTo(10);
    }

    // Add more tests for relationships (e.g., Player's inventory, Monster's drops)
}
    */