package io.papermc.aup.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.aup.Helper;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class PlayerLeaveHandling implements Listener {
    
    // Handle players leaving the server
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Helper.gameRunning == false) {
            return;
        }

        String name = event.getPlayer().getDisplayName();

        Helper.death(name);

        // If there are no impostors left, end the game
        // If there is an impostor, do nothing
        for (AmongUsPlayer a : Helper.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }

        Helper.gameRunning = false;
        Component c = Component.text("Game Ended: There are no more impostors!", NamedTextColor.RED);
        Bukkit.broadcast(c);
    }

}
