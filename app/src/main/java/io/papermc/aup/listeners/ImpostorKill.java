package io.papermc.aup.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

public class ImpostorKill implements Listener {

    // Ensure that the impostor can kill with one click
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if (!(damager instanceof Player)) { return; }
        if (!Game.gameRunning) { return; }
            
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.getDisplayName().equals(damager.getName())) {
               Game.killPlayer(victim);
            }
        }
    }
}
