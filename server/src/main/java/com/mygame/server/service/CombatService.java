package com.mygame.server.service;

/* 
import com.mygame.server.dto.GameStateDTO;
import com.mygame.server.model.Monster;
import com.mygame.server.model.Player;
import com.mygame.server.repository.MonsterRepository;
import com.mygame.server.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CombatService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MonsterRepository monsterRepository;

    public GameStateDTO attack(Long playerId, Long roomId) {
        Player player = playerRepository.findById(playerId).orElseThrow();
        // 假設你的 MonsterRepository 有一個根據 roomId 查詢 Monster 的方法
        Monster monster = monsterRepository.findByRoomId(roomId).orElseThrow();

        // 原AttackCommand邏輯
        int playerDamage = player.getAttack();
        monster.takeDamage(playerDamage);
        // 假設 Player 類別有 addDamage 方法來累計總傷害
        // 注意：你之前的 Player 類別沒有 addDamage 方法，你需要添加這個方法
        player.setTotalDamage(player.getTotalDamage() + playerDamage);

        // 保存狀態
        monsterRepository.save(monster);
        playerRepository.save(player);

        return new GameStateDTO(player, monster);
    }
}*/
