package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.papermc.aup.Game;

public class DeathMessageHandler implements Listener {

    // Prevent publicisation of player deaths
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (Game.gameRunning) {
            event.deathMessage(null);
        }
    }
}
