package com.mygame.server.repository;

import com.mygame.server.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByName(String name); // 可以根據玩家名稱查找
}
