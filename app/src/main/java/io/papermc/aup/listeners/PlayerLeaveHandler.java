package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.aup.Game;

public class PlayerLeaveHandler implements Listener {
    
    // Handle players leaving the server
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // If game is not running -> ignore
        if (Game.gameRunning == false) {
            return;
        }

        Game.killPlayer(event.getPlayer());
    }
}
