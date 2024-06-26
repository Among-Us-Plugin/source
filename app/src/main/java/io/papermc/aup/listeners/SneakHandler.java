package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import io.papermc.aup.Game;
import io.papermc.aup.interactions.Vent;

public class SneakHandler implements Listener {
    
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!Game.gameRunning) { return; }
        if (event.isSneaking()) {
            Vent.handleSneak(event.getPlayer());
        }
    }

}
