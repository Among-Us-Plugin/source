package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class PlayerDeathHandling implements Listener {

    // Handle player deaths
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // If game is not running -> ignore
        if (Game.gameRunning == false) {
            return;
        }

        String name = event.getPlayer().getDisplayName();
        Game.death(name);
        Game.checkCrewmates();
        Game.checkImpostors();
    }
}
