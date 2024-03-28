package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.aup.Helper;

@SuppressWarnings("deprecation")
public class PlayerLeaveHandling implements Listener {
    
    // Handle players leaving the server
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // If game is not running -> ignore
        if (Helper.gameRunning == false) {
            return;
        }

        String name = event.getPlayer().getDisplayName();
        Helper.death(name);
        Helper.checkCrewmates();
        Helper.checkImpostors();
    }
}
