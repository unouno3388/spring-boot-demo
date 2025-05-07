
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
        context.setPlayer(player);
        Room currentRoom = roomService.getRoomById(player.getCurrentRoomId());
        System.out.println("根據 currentRoomId: " + player.getCurrentRoomId() + 
                          " 獲取的房間: " + (currentRoom != null ? currentRoom.getId() : "null"));
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
            String result = command.execute(context.getPlayer(), context);

            // 同步玩家狀態到資料庫和 session
            playerRepository.save(context.getPlayer());
            Player updatedPlayer = playerService.findByName(context.getPlayer().getName());
            context.setPlayer(updatedPlayer);
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
/* 
package com.mygame.server.service;

 import com.mygame.server.command.Command;
 import com.mygame.server.command.CommandParser;
 import com.mygame.server.context.GameContext;
 import com.mygame.server.initializer.GameInitializer;
 //import com.mygame.server.model.Item;
 import com.mygame.server.model.Player;
 import com.mygame.server.model.Room;
 import com.mygame.server.repository.PlayerRepository;
 //import com.mygame.server.repository.ItemRepository; // 移除 ItemRepository 的 import
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 //import java.util.List;
 //import java.util.stream.Collectors;

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
    //private GameContext initializeNewGame(String playerName) {
        // 透過 GameInitializer 初始化遊戲世界
        /* 
        GameContext context = gameInitializer.init();
        
        // 創建玩家並設置初始狀態
        Player player = playerService.createNewPlayer(
            playerName,
            100,  // baseHp
            10,   // baseAttack
            5     // baseDefense
        );
        context.setPlayer(player);
        
        // 設置初始房間
        Room startRoom = roomService.createNewRoom(
            "初始房間",
            "這是一個充滿冒險氣息的起點。"
        );
        context.setCurrentRoom(startRoom);
        playerService.setCurrentRoomId(player, startRoom.getId());
        
        return context;
        
     //   return null;
    //}
    @Transactional
    public GameContext loadExistingGame(Player player) {
        System.out.println("載入現有玩家: " + player.getName() + ", ID: " + player.getId() + ", currentRoomId: " + player.getCurrentRoomId());
        GameContext context = new GameContext(playerService, roomService);
        context.setPlayer(player);
        Room currentRoom = roomService.getRoomById(player.getCurrentRoomId());
        System.out.println("根據 currentRoomId: " + player.getCurrentRoomId() + " 獲取的房間: " + currentRoom);
        context.setCurrentRoom(currentRoom);
        return context;
        /* 
        GameContext context = new GameContext(playerService, roomService);
        
        // 載入玩家狀態
        context.setPlayer(player);
        
        // 載入當前房間
        Long currentRoomId = player.getCurrentRoomId();
        Room currentRoom = roomService.getRoomById(currentRoomId); // 假設 RoomService 有這個方法
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
            String result = command.execute(context.getPlayer(), context);

            // **重要：在執行命令後，立即同步玩家狀態到資料庫和 session**
            playerRepository.save(context.getPlayer()); // 更新資料庫

            // 從資料庫重新讀取玩家
            Player updatedPlayer = playerService.findByName(context.getPlayer().getName()); // 或者使用 ID 查詢
            context.setPlayer(updatedPlayer); // 更新 session 中的玩家

            return result;

        } catch (Exception e) {
            return "執行命令時發生錯誤: " + e.getMessage();
        }
}

    @Transactional
    public void saveGame(GameContext context) {
        // 保存玩家狀態
        Player player = context.getPlayer();
        playerService.setCurrentRoomId(player, context.getCurrentRoom().getId());
        
        // 其他需要保存的狀態可以在此添加
        playerRepository.save(player);
        //playerRepository.savePlayer(player);
    }
}*/