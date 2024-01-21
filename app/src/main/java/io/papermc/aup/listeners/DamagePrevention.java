package io.papermc.aup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.papermc.aup.Helper;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;

public class DamagePrevention implements Listener {

    // Prevent Crewmates from attacking others during the game
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (Helper.gameRunning == false) {
                return;
            }
            for (AmongUsPlayer a : Helper.amongUsPlayers) {
                if (a instanceof Crewmate && a.getPlayerName().equals(event.getDamager().getName())) {
                    event.setCancelled(true);
                }
            }            
        }
    }

}
