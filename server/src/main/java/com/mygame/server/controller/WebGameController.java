package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("context")

public class WebGameController {

    private final GameService gameService;

    @Autowired
    public WebGameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 顯示名稱輸入表單
    @GetMapping("/")
    public String showNameInput(Model model) {
        model.addAttribute("playerName", "");
        return "nameInput";
    }

    // 處理遊戲開始
    // 處理遊戲開始
    @PostMapping("/start-game")
    public String startGame(
            @RequestParam("playerName") String playerName,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // 驗證輸入
        if (playerName == null || playerName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "請輸入有效的玩家名稱");
            return "redirect:";  // 相對路徑返回當前URL
        }

        try {
            GameContext context = gameService.startGame(playerName);
            session.setAttribute("context", context);
            redirectAttributes.addFlashAttribute("message", "遊戲開始！");
            return "redirect:game-ui";  // 改為相對路徑
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "遊戲初始化失敗: " + e.getMessage());
            return "redirect:";  // 相對路徑返回當前URL
        }
    }

    // 顯示遊戲界面
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session) {
        System.out.println("尝试访问/game-ui端点");
        GameContext context = (GameContext) session.getAttribute("context");
        if (context == null) {
            System.out.println("未找到游戏上下文，重定向到首页");
            return "redirect:/";
        }
        System.out.println("当前房间: " + context.getCurrentRoom().getName());
        return "game";
    }

    // 處理遊戲指令輸入
    @PostMapping("/game-action")
    public String handleInput(
            @RequestParam("input") String input,
            @SessionAttribute("context") GameContext context,
            RedirectAttributes redirectAttributes) {
        
        try {
            String gameOutput = gameService.handleInput(input, context);
            redirectAttributes.addFlashAttribute("output", gameOutput);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "指令執行失敗: " + e.getMessage());
        }
        
        return "redirect:/game-ui";
    }

    // 保存遊戲
    @PostMapping("/save-game")
    public String saveGame(
            @SessionAttribute("context") GameContext context,
            RedirectAttributes redirectAttributes) {
        
        try {
            gameService.saveGame(context);
            redirectAttributes.addFlashAttribute("message", "遊戲已成功保存！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失敗: " + e.getMessage());
        }
        
        return "redirect:/game-ui";
    }

    // 處理session過期或無效的情況
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "會話已過期，請重新開始遊戲");
        return "redirect:/";
    }
}