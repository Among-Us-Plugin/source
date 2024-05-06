package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.papermc.aup.Game;

public class PlayerDeathHandler implements Listener {

    // Handle player deaths
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Game.gameRunning) { return; }
        Game.killPlayer(event.getPlayer());
    }
}
