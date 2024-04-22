package io.papermc.aup.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
        if (event.getHand().equals(EquipmentSlot.HAND)) { return; }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block.getType().equals(Material.LODESTONE)) {
            FlipSwitches.run(player);
        } if (block.getType().equals(Material.SCULK)) {
            Colors.run(player);
        } if (block.getType().equals(Material.PLAYER_HEAD)) {
            if ( !AmongUsPlayer.getAmongUsPlayerByDisplayName(event.getPlayer().getDisplayName()).isALive() ) { return; }
            EmergencyMeeting.run(block);
        } if (block.getType().equals(Material.IRON_TRAPDOOR)) {            
            AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
            if ( a instanceof Crewmate ) { return; }
            if ( !a.isALive() ) { return; }
            Vent.run(player, block);
        }
    }
}
