package com.mygame.server.service;

/*& 
import com.mygame.server.context.GameContext;
import com.mygame.server.model.Player;
import com.mygame.server.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameSessionService {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoomService roomService;

    private Map<String, GameContext> sessions = new ConcurrentHashMap<>();

    public String startNewGame(String playerName) {
        String sessionId = UUID.randomUUID().toString();
        Player player = playerService.createPlayer(playerName);
        Room startRoom = roomService.getStartingRoom();

        sessions.put(sessionId, new GameContext(player, startRoom));
        return sessionId;
    }

    public GameContext getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    // 你可能還需要其他管理 session 的方法，例如根據 sessionId 獲取 GameContext 等
    
}
*/