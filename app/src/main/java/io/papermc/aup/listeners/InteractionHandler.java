package io.papermc.aup.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import io.papermc.aup.interactions.Colors;
import io.papermc.aup.interactions.EmergencyMeeting;
import io.papermc.aup.interactions.FlipSwitches;
import io.papermc.aup.interactions.Vent;

@SuppressWarnings("deprecation")
public class InteractionHandler implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Game.gameRunning) { return; }
        if ( Game.emergencyMeetingInProgress ) { return; }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (event.getHand().equals(EquipmentSlot.HAND)) { return; }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());

        if (matchingMaterial(block, Material.LODESTONE)) {
            if (a instanceof Impostor) {
                sendImpostorTaskError(player);
                return;
            }
            FlipSwitches.run(player);
        } if (matchingMaterial(block, Material.SCULK)) {
            if (a instanceof Impostor) {
                sendImpostorTaskError(player);
                return;
            }
            Colors.run(player);
        } if (matchingMaterial(block, Material.PLAYER_HEAD)) {
            if ( playerIsDead(event) ) { return; }
            EmergencyMeeting.run(player, block);
        } if (matchingMaterial(block, Material.IRON_TRAPDOOR)) {            
            if ( a instanceof Crewmate ) { return; }
            if ( playerIsDead(event) ) { return; }
            Vent.run(player, block);
        }
    }

    private void sendImpostorTaskError(Player player) {
        Broadcasting.sendError(player, "You can't do tasks! You are an impostor!");
    }

    private boolean playerIsDead(PlayerInteractEvent event) {
        return !AmongUsPlayer.getAmongUsPlayerByDisplayName(event.getPlayer().getDisplayName()).isALive();
    }

    private boolean matchingMaterial(Block block, Material material) {
        return block.getType().equals(material);
    }
}
