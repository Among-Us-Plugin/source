package io.papermc.aup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;
import io.papermc.aup.interactions.Vent;

@SuppressWarnings("deprecation")
public class MovementHandler implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!Game.gameRunning) { return; }

        if (Game.emergencyMeetingInProgress) {
            if (!playerIsMovingHorizontally(event)) { return; }
            event.setCancelled(true);
        }

        Player p = event.getPlayer();
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(p.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        if ( !impostor.isVenting()) { return; }
        
        if (playerIsMovingHorizontally(event)) {
            if (!Vent.handleMove(p, impostor)) {
                event.setCancelled(true);
            }
        } else if (playerIsMovingVertially(event)) {
            event.setCancelled(true);
        }
    }

    private boolean playerIsMovingVertially(PlayerMoveEvent event) {
        return event.getFrom().getY() != event.getTo().getY();
    }

    private boolean playerIsMovingHorizontally(PlayerMoveEvent event) {
        return event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ();
    }
}
