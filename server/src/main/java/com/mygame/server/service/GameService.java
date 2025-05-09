package com.mygame.server.service;

import com.mygame.server.command.Command;
import com.mygame.server.command.CommandParser;
import com.mygame.server.context.GameContext;
import com.mygame.server.initializer.GameInitializer;
import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import com.mygame.server.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {
    private final GameInitializer gameInitializer;
    private final PlayerService playerService;
    private final RoomService roomService;
    private final CommandParser commandParser;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(
        GameInitializer gameInitializer,
        PlayerService playerService,
        RoomService roomService,
        CommandParser commandParser,
        PlayerRepository playerRepository 
    ) {
        this.gameInitializer = gameInitializer;
        this.playerService = playerService;
        this.roomService = roomService;
        this.commandParser = commandParser;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public GameContext startGame(String playerName) {
        Player existingPlayer = playerService.findByName(playerName);
        if (existingPlayer != null) {
            System.out.println("找到現有玩家: " + playerName + ", 載入現有遊戲。");
            return loadExistingGame(existingPlayer);
        } else {
            System.out.println("未找到玩家: " + playerName + ", 創建新遊戲。");
            return gameInitializer.init(playerName);
        }
    }   

    public Player findPlayerByName(String playerName) {
        return playerService.findByName(playerName);
    }

    @Transactional
    public GameContext loadExistingGame(Player player) {
        System.out.println("載入現有玩家: " + player.getName() + ", ID: " + player.getId() + 
                          ", currentRoomId: " + player.getCurrentRoomId());
        GameContext context = new GameContext(playerService, roomService);
        // 顯式觸發 equipment 的加載
        if (player != null) {
            player.getEquipment().size(); // 確保在事務內初始化
            player.getInventory().size();
        }
        context.setPlayer(player);
        Room currentRoom = roomService.getRoomById(player.getCurrentRoomId());
        System.out.println("根據 currentRoomId: " + player.getCurrentRoomId() + 
                          " 獲取的房間: " + (currentRoom != null ? currentRoom.getId() : "null"));
        if (currentRoom != null) {
            // 顯式觸發 exits 的加載
            currentRoom.getExits().size(); // 確保在事務內初始化
        }
        context.setCurrentRoom(currentRoom);
        return context;
    }

    @Transactional
    public String handleInput(String input, GameContext context) {
        Command command = commandParser.parse(input);
        if (command == null) {
            return "未知的命令。請輸入 'help' 查看可用命令。";
        }

        try {
            // 確保在事務內觸發 exits 加載（如果命令需要）
            if (context.getCurrentRoom() != null) {
                context.getCurrentRoom().getExits().size();
            }
            if (context.getPlayer() != null) {
                context.getPlayer().getEquipment().size(); // 確保在事務內初始化
                context.getPlayer().getInventory().size();
            }
            String result = command.execute(context.getPlayer(), context);
            playerRepository.save(context.getPlayer());
            //Player updatedPlayer = playerService.findByName(context.getPlayer().getName());
            //context.setPlayer(updatedPlayer);
            return result;
        } catch (Exception e) {
            return "執行命令時發生錯誤: " + e.getMessage();
        }
    }

    @Transactional
    public void saveGame(GameContext context) {
        Player player = context.getPlayer();
        Room currentRoom = context.getCurrentRoom();
        System.out.println("Saving game for player: " + player.getName() + 
                          ", currentRoomId: " + (currentRoom != null ? currentRoom.getId() : "null"));
        playerService.setCurrentRoomId(player, currentRoom != null ? currentRoom.getId() : null);
        playerRepository.save(player);
        System.out.println("Game saved for player: " + player.getName());
    }
}