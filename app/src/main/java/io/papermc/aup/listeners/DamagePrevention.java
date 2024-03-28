package io.papermc.aup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;

public class DamagePrevention implements Listener {

    // Prevent Crewmates from attacking others during the game
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (Game.gameRunning == false) {
                return;
            }
            for (AmongUsPlayer a : Game.amongUsPlayers) {
                if (a instanceof Crewmate && a.getPlayerName().equals(event.getDamager().getName())) {
                    event.setCancelled(true);
                }
            }            
        }
    }

}
