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
     private final PlayerRepository playerRepository;
     private final RoomService roomService;
     private final PlayerService playerService;
     //private final ItemService itemService;
     private final CommandParser commandParser;
     //private final ItemRepository itemRepository; // 移除 ItemRepository

     @Autowired
     public GameService(GameInitializer gameInitializer,
                        PlayerRepository playerRepository,
                        RoomService roomService,
                        PlayerService playerService,
                        //ItemService itemService,
                        CommandParser commandParser) { // 移除 ItemRepository from constructor
         this.gameInitializer = gameInitializer;
         this.playerRepository = playerRepository;
         this.roomService = roomService;
         this.playerService = playerService;
         //this.itemService = itemService;
         this.commandParser = commandParser;
         //this.itemRepository = null; // 移除 ItemRepository 的賦值
     }

     @Transactional
     public GameContext startGame(String playerName) {
         Player player = playerRepository.findByName(playerName);
         GameContext context;

         if (player == null) {
             // If no player found, create a new game
             context = gameInitializer.init();
             player = context.getPlayer();
             playerService.createNewPlayer(player.getName(), player.getHp(), player.getBaseAttack(), player.getDefense());
             player.setName(playerName);
             playerRepository.save(player);

             // Save initial room via service
             Room startRoom = context.getCurrentRoom();
             roomService.setExit(startRoom, "north", startRoom);
             playerService.setCurrentRoomId(player, startRoom.getId());
             playerRepository.save(player);

         } else {
             // If player exists, load game state
             context = new GameContext();
             context.setPlayer(player);

             // Load player's current room
             Room currentRoom = roomService.getExit(roomService.createNewRoom("", ""), "north"); // Assuming RoomService has getRoomById or similar
             context.setCurrentRoom(currentRoom);

             // Load player's inventory and equipment (now managed by Player entity)
             // No need to use ItemRepository here, they should be loaded with the Player
         }
         return context;
     }

     public String handleInput(String input, GameContext context) {
         Command command = commandParser.parse(input);
         if (command != null) {
             return command.execute(context.getPlayer(), context);
         } else {
             return "未知的命令。請輸入 'help' 查看可用命令。\n";
         }
     }

     @Transactional
     public void saveGame(GameContext context) {
         Player player = context.getPlayer();
         playerService.setCurrentRoomId(player, context.getCurrentRoom().getId());
         playerRepository.save(player);

         // Save current room (if needed, RoomService might handle this)
         //roomService.saveRoom(context.getCurrentRoom());

         // Player's inventory and equipment should be saved automatically
         // if the Player entity has @ManyToMany relationships configured correctly
         // and cascading is enabled if necessary.
         // We don't need to manually manage them here anymore with ItemRepository.

         //TODO: Save other game state (monsters, etc.)
     }
 }