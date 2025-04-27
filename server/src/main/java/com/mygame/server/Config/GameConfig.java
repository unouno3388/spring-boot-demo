// GameConfig.java
package com.mygame.server.Config;

import com.mygame.server.model.Player;
//import com.mygame.server.GameState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class GameConfig {
    
    //@Bean
    //@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
   // public Player player() {
    //    return new Player("玩家", 100, 15, 5);
   // }
    
    //@Bean
    //@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    //public GameState gameState() {
      //  return new GameState();
    //}
}