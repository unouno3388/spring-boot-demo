package com.mygame.server.command;

import com.mygame.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandParser {

    private final RoomService roomService;
    private final MonsterService monsterService;
    private final PlayerService playerService;
    private final SkillService skillService;

    @Autowired
    public CommandParser(RoomService roomService, MonsterService monsterService,
                         PlayerService playerService, SkillService skillService) {
        this.roomService = roomService;
        this.monsterService = monsterService;
        this.playerService = playerService;
        this.skillService = skillService;
    }

    public Command parse(String input) {
        String inputLower = input.toLowerCase();
        if (inputLower.startsWith("move ")) return new MoveCommand(input.substring(5), roomService, monsterService);
        if (inputLower.equals("attack")) return new AttackCommand(monsterService, playerService);
        if (inputLower.equals("look")) return new LookCommand(roomService, monsterService);
        if (inputLower.equals("use potion")) return new UsePotionCommand(playerService, roomService);
        if (inputLower.startsWith("skill ")) return new SkillCommand(input.substring(6), skillService, monsterService, playerService);
        if (inputLower.equals("exit")) return new ExitCommand();
        if (inputLower.startsWith("equip ")) {
            String itemName = input.substring(6).trim(); // 提取物品名稱
            return new EquipCommand(itemName, playerService);
        }
        if (inputLower.startsWith("unequip ")) {
            String itemName = input.substring(8).trim(); // 提取物品名稱
            return new UnequipCommand(itemName, playerService);
        }
        if (inputLower.equals("bag")) return new InventoryCommand(playerService);
        if (input.equals("equipment")) return new EquipmentCommand(playerService);
        if (input.equals("show stats")) return new ShowStatsCommand(playerService);
        return null;
    }
}