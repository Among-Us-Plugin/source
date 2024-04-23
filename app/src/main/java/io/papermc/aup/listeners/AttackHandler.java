package io.papermc.aup.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;

public class AttackHandler implements Listener {

    // Prevent Crewmates from attacking others during the game
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();

        if (!(damager instanceof Player)) { return; }
        if (!Game.gameRunning) { return; }
            
        cancelEventIfCrewmate(event, damager);
    }

    private void cancelEventIfCrewmate(EntityDamageByEntityEvent event, Entity damager) {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (isCrewmate(damager, a)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isCrewmate(Entity damager, AmongUsPlayer a) {
        return a instanceof Crewmate && a.getDisplayName().equals(damager.getName());
    }
}
