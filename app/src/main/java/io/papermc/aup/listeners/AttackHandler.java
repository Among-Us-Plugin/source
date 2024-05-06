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

        if (!Game.gameRunning) { return; }
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) { return; }
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(damager.getName());
        if (a instanceof Crewmate) {
            event.setCancelled(true);
        }
    }
}
