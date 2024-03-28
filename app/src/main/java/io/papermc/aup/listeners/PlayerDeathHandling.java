package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.papermc.aup.Helper;

@SuppressWarnings("deprecation")
public class PlayerDeathHandling implements Listener {

    // Handle player deaths
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
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
