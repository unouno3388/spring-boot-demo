package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebGameController {

    private final GameService gameService;

    @Autowired
    public WebGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showNameInput(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        model.addAttribute("playerName", "");
        return "nameInput";
    }

    @PostMapping("/start-game")
    public String startGame(
            @RequestParam String playerName, 
            HttpSession session, 
            HttpServletRequest request, 
            HttpServletResponse response, 
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Starting game for player: " + playerName);
            session.invalidate();
            session = request.getSession(true);
            System.out.println("New session created: " + session.getId());

            Player player = gameService.findPlayerByName(playerName);
            GameContext context;
            if (player == null) {
                context = gameService.startGame(playerName);
                redirectAttributes.addFlashAttribute("message", "歡迎，" + playerName + "!");
            } else {
                context = gameService.loadExistingGame(player);
                redirectAttributes.addFlashAttribute("message", "歡迎回來，" + playerName + "!");
            }
            if (context == null || context.getPlayer() == null) {
                throw new IllegalStateException("Failed to create or load game context for player: " + playerName);
            }
            session.setAttribute("gameContext", context);
            System.out.println("Game context set for player: " + context.getPlayer().getName());
            return "redirect:/game-ui";
        } catch (Exception e) {
            System.out.println("Error in startGame: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "初始化失敗: " + e.getMessage());
            return "redirect:/";
        }
    }
    @Transactional
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            model.addAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        System.out.println("Rendering game UI for player: " + context.getPlayer().getName() + 
                          ", currentRoomId: " + (context.getCurrentRoom() != null ? context.getCurrentRoom().getId() : "null"));
        model.addAttribute("context", context);
        model.addAttribute("player", context.getPlayer());

        if (!model.containsAttribute("output")) {
            model.addAttribute("output", "請輸入指令...");
        }

        return "game";
    }

    @PostMapping("/game-action")
    public String handleInput(
            @RequestParam("input") String input,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            redirectAttributes.addFlashAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        try {
            String gameOutput = gameService.handleInput(input, context);
            if (gameOutput == null || gameOutput.isEmpty()) {
                gameOutput = "無效指令，請重試！";
            }
            System.out.println("Handling input for player: " + context.getPlayer().getName() + ", Input: " + input);
            redirectAttributes.addFlashAttribute("output", gameOutput);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "指令執行失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    @PostMapping("/save-game")
    public String saveGame(HttpSession session, RedirectAttributes redirectAttributes) {
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            redirectAttributes.addFlashAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/game-ui";
        }

        try {
            System.out.println("Saving game in controller for player: " + context.getPlayer().getName());
            gameService.saveGame(context);
            System.out.println("Game saved successfully in controller for player: " + context.getPlayer().getName());
            redirectAttributes.addFlashAttribute("message", "遊戲已成功保存！");
        } catch (Exception e) {
            System.out.println("Error in saveGame controller: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "保存失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "會話已過期，請重新開始遊戲");
        return "redirect:/";
    }
}
/* 
package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebGameController {

    private final GameService gameService;

    @Autowired
    public WebGameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 顯示名稱輸入表單
    @GetMapping("/")
    public String showNameInput(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        model.addAttribute("playerName", "");
        return "nameInput";
    }

    // 處理遊戲開始
    @PostMapping("/start-game")
    public String startGame(
            @RequestParam String playerName, 
            HttpSession session, 
            HttpServletRequest request, 
            HttpServletResponse response, 
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Starting game for player: " + playerName);
            // 清除舊 session
            session.invalidate();
            session = request.getSession(true);
            System.out.println("New session created: " + session.getId());

            Player player = gameService.findPlayerByName(playerName);
            GameContext context;
            if (player == null) {
                context = gameService.startGame(playerName);
                redirectAttributes.addFlashAttribute("message", "歡迎，" + playerName + "!");
            } else {
                context = gameService.loadExistingGame(player);
                redirectAttributes.addFlashAttribute("message", "歡迎回來，" + playerName + "!");
            }
            if (context == null || context.getPlayer() == null) {
                throw new IllegalStateException("Failed to create or load game context for player: " + playerName);
            }
            session.setAttribute("gameContext", context);
            System.out.println("Game context set for player: " + context.getPlayer().getName());
            return "redirect:/game-ui";
        } catch (Exception e) {
            System.out.println("Error in startGame: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "初始化失敗: " + e.getMessage());
            return "redirect:/";
        }
    }

    // 顯示遊戲界面
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            model.addAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        model.addAttribute("context", context);
        model.addAttribute("player", context.getPlayer());

        if (!model.containsAttribute("output")) {
            model.addAttribute("output", "請輸入指令...");
        }

        return "game";
    }

    // 處理遊戲指令輸入
    @PostMapping("/game-action")
    public String handleInput(
            @RequestParam("input") String input,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            redirectAttributes.addFlashAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        try {
            String gameOutput = gameService.handleInput(input, context);
            if (gameOutput == null || gameOutput.isEmpty()) {
                gameOutput = "無效指令，請重試！";
            }
            System.out.println("Handling input for player: " + context.getPlayer().getName() + ", Input: " + input);
            redirectAttributes.addFlashAttribute("output", gameOutput);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "指令執行失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 保存遊戲
    @PostMapping("/save-game")
    public String saveGame(HttpSession session, RedirectAttributes redirectAttributes) {
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            redirectAttributes.addFlashAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        try {
            gameService.saveGame(context);
            redirectAttributes.addFlashAttribute("message", "遊戲已成功保存！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 處理 session 過期或無效的情況
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "會話已過期，請重新開始遊戲");
        return "redirect:/";
    }
}
/* 
package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("gameContext")
public class WebGameController {

    private final GameService gameService;

    @Autowired
    public WebGameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 顯示名稱輸入表單
    @GetMapping("/")
    public String showNameInput(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        model.addAttribute("playerName", "");
        return "nameInput";
    }

    // 處理遊戲開始
    @PostMapping("/start-game")
    public String startGame(@RequestParam String playerName, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            // 清除所有相關 session 數據
            session.invalidate();
            session = request.getSession(true);

            Player player = gameService.findPlayerByName(playerName);
            GameContext context;
            if (player == null) {
                context = gameService.startGame(playerName);
                redirectAttributes.addFlashAttribute("message", "歡迎，" + playerName + "!");
            } else {
                context = gameService.loadExistingGame(player);
                redirectAttributes.addFlashAttribute("message", "歡迎回來，" + playerName + "!");
            }
            session.setAttribute("gameContext", context);
            System.out.println("Starting game for player: " + playerName + ", Context Player Name: " + ((context != null && context.getPlayer() != null) ? context.getPlayer().getName() : "null"));
            return "redirect:/game-ui";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "初始化失敗: " + e.getMessage());
            return "redirect:/";
        }
    }

    // 顯示遊戲界面
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            model.addAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        model.addAttribute("context", context);
        model.addAttribute("player", context.getPlayer());

        if (!model.containsAttribute("output")) {
            model.addAttribute("output", "請輸入指令...");
        }

        return "game";
    }

    // 處理遊戲指令輸入
    @PostMapping("/game-action")
    public String handleInput(
            @RequestParam("input") String input,
            @SessionAttribute("gameContext") GameContext context,
            RedirectAttributes redirectAttributes) {
        try {
            String gameOutput = gameService.handleInput(input, context);
            if (gameOutput == null || gameOutput.isEmpty()) {
                gameOutput = "無效指令，請重試！";
            }
            // 確認當前上下文的玩家
            System.out.println("Handling input for player: " + context.getPlayer().getName() + ", Input: " + input);
            redirectAttributes.addFlashAttribute("output", gameOutput);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "指令執行失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 保存遊戲
    @PostMapping("/save-game")
    public String saveGame(
            @SessionAttribute("gameContext") GameContext context,
            RedirectAttributes redirectAttributes) {
        try {
            gameService.saveGame(context);
            redirectAttributes.addFlashAttribute("message", "遊戲已成功保存！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 處理 session 過期或無效的情況
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "會話已過期，請重新開始遊戲");
        return "redirect:/";
    }
}
*/
/*
package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("gameContext")
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
    @PostMapping("/start-game")
    public String startGame(@RequestParam String playerName, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Player player = gameService.findPlayerByName(playerName);
            GameContext context;
            if (player == null) {
                context = gameService.startGame(playerName);
                redirectAttributes.addFlashAttribute("message", "歡迎，" + playerName + "!");
            } else {
                context = gameService.loadExistingGame(player);
                redirectAttributes.addFlashAttribute("message", "歡迎回來，" + playerName + "!");
            }
            session.setAttribute("gameContext", context);
            session.removeAttribute("output"); // 清除之前的輸出
            return "redirect:/game-ui";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "初始化失敗: " + e.getMessage());
            return "redirect:/";
        }
    }

    // 顯示遊戲界面
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session) {
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context == null || context.getPlayer() == null) {
            model.addAttribute("error", "會話無效，請重新開始遊戲");
            return "redirect:/";
        }

        // 添加 context 和 player 到 model
        model.addAttribute("context", context);
        model.addAttribute("player", context.getPlayer());

        // 檢查 flash 屬性中的 output（來自 handleInput 或其他操作）
        if (!model.containsAttribute("output")) {
            model.addAttribute("output", "請輸入指令...");
        }

        return "game";
    }

    // 處理遊戲指令輸入
    @PostMapping("/game-action")
    public String handleInput(
            @RequestParam("input") String input,
            @SessionAttribute("gameContext") GameContext context,
            RedirectAttributes redirectAttributes) {
        try {
            String gameOutput = gameService.handleInput(input, context);
            if (gameOutput == null || gameOutput.isEmpty()) {
                gameOutput = "無效指令，請重試！";
            }
            redirectAttributes.addFlashAttribute("output", gameOutput);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "指令執行失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 保存遊戲
    @PostMapping("/save-game")
    public String saveGame(
            @SessionAttribute("gameContext") GameContext context,
            RedirectAttributes redirectAttributes) {
        try {
            gameService.saveGame(context);
            redirectAttributes.addFlashAttribute("message", "遊戲已成功保存！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失敗: " + e.getMessage());
        }
        return "redirect:/game-ui";
    }

    // 處理 session 過期或無效的情況
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleSessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "會話已過期，請重新開始遊戲");
        return "redirect:/";
    }
}
    */
/* 
package com.mygame.server.controller;

import com.mygame.server.service.GameService;
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
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
    @PostMapping("/start-game")
    public String startGame(@RequestParam String playerName, HttpSession session, RedirectAttributes redirectAttributes) {
        Player player = gameService.findPlayerByName(playerName);
        if (player == null) {
            GameContext newContext = gameService.startGame(playerName);
            session.setAttribute("gameContext", newContext); // 確保新的 context 放入 session
            redirectAttributes.addFlashAttribute("message", "歡迎，" + playerName + "!");
        } else {
            GameContext existingContext = gameService.loadExistingGame(player);
            session.setAttribute("gameContext", existingContext); // 確保現有的 context 放入 session
            redirectAttributes.addFlashAttribute("message", "歡迎回來，" + playerName + "!");
        }
        session.removeAttribute("output"); // 清除之前的輸出
        session.removeAttribute("error");
        return "redirect:/game-ui";
    }
    /* 
    // 處理遊戲開始
    @PostMapping("/start-game")
    public String startGame(
            @RequestParam("playerName") String playerName,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Player existingPlayer = gameService.findPlayerByName(playerName); // 使用 GameService 的方法
            System.out.println("WebGameController.startGame() findPlayerByName(" + playerName + ") 結果: " + existingPlayer);
            GameContext context = gameService.startGame(playerName);
            session.setAttribute("context", context);
            redirectAttributes.addFlashAttribute("message", "遊戲開始！");
            return "redirect:/game-ui";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "初始化失敗: " + e.getMessage());
            return "redirect:/";
        }
    }
  
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session) {
        GameContext context = (GameContext) session.getAttribute("gamContext");
        if (context == null || context.getPlayer() == null) {
            return "redirect:/";
        }   
        model.addAttribute("player", context.getPlayer());
        // 从session获取输出并添加到model
        model.addAttribute("output", session.getAttribute("output"));
        model.addAttribute("context", context);
        
        // 清除session中的临时属性
        session.removeAttribute("output");
        
        return "game";
    }
    /* 
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        GameContext context = (GameContext) session.getAttribute("gameContext");
        if (context != null && context.getPlayer() != null) {
            model.addAttribute("context", context);
            model.addAttribute("output", session.getAttribute("output"));
            model.addAttribute("message", session.getAttribute("message"));
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("output");
            session.removeAttribute("message");
            session.removeAttribute("error");
            return "game";
        } else {
            return "redirect:/";
        }
    }*/
    /* 
    // 顯示遊戲界面
    @GetMapping("/game-ui")
    public String showGameUI(Model model, HttpSession session) {
        GameContext context = (GameContext) session.getAttribute("context");
        if (context == null) {
            return "redirect:/";
        }
        
        model.addAttribute("player", context.getPlayer());
        model.addAttribute("room", context.getCurrentRoom());
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

*/