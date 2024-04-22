package io.papermc.aup.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.interactions.Colors;
import io.papermc.aup.interactions.EmergencyMeeting;
import io.papermc.aup.interactions.FlipSwitches;
import io.papermc.aup.interactions.Vent;

@SuppressWarnings("deprecation")
public class InteractionHandler implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Game.gameRunning) { return; }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }

        if (event.getClickedBlock().getType().equals(Material.LODESTONE)) {
            Player player = event.getPlayer();
            FlipSwitches.run(player);
        } if (event.getClickedBlock().getType().equals(Material.SCULK)) {
            Player player = event.getPlayer();
            Colors.run(player);
        } if (event.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) {
            if ( !AmongUsPlayer.getAmongUsPlayerByDisplayName(event.getPlayer().getDisplayName()).isALive() ) { return; }
            Block block = event.getClickedBlock();
            EmergencyMeeting.run(block);
        } if (event.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {            
            Player p = event.getPlayer();
            AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(p.getDisplayName());
            if ( a instanceof Crewmate ) { return; }
            if ( !a.isALive() ) { return; }
            Vent.run(p);
        }
    }
}
