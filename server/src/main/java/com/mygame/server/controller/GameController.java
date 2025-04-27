package com.mygame.server.controller;


import com.mygame.server.service.*;
import com.mygame.server.context.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
/*

@RestController
public class GameController {

  @Autowired
  private GameService gameService;

  @PostMapping("/game")
  public String handleCommand(@RequestBody Map<String, String> payload) {
  String command = payload.get("command");
  if (command == null) {
  return "Error: No command provided.";
  }
  GameContext context = new GameContext(); // 你需要根據你的應用邏輯獲取或創建 GameContext
  return gameService.handleInput(command, context);
  }
}
*/