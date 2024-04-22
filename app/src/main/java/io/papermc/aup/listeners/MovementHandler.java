package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.aup.Game;

public class MovementHandler implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!Game.gameRunning) { return; }

        if (Game.emergencyMeetingInProgress) {
            if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) { return; }
            event.setCancelled(true);
        }
        
    }
}
