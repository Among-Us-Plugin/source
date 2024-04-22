package io.papermc.aup.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class MovementHandler implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!Game.gameRunning) { return; }

        if (Game.emergencyMeetingInProgress) {
            if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) { return; }
            event.setCancelled(true);
        }

        Player p = event.getPlayer();
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(p.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        if ( !impostor.isVenting()) { return; }
        event.setCancelled(true);
    }
}
