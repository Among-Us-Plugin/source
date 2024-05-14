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
import io.papermc.aup.interactions.EmergencyMeeting;
import io.papermc.aup.interactions.Vent;
import io.papermc.aup.interactions.tasks.Colors;
import io.papermc.aup.interactions.tasks.FlipSwitches;
import io.papermc.aup.interactions.tasks.PushButton;

@SuppressWarnings("deprecation")
public class InteractionHandler implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Game.gameRunning) { return; }
        if ( Game.emergencyMeetingInProgress ) { return; }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) { return; }
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) { return; }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());

        if (matchingMaterial(block, FlipSwitches.blockMaterial)) {
            if (a instanceof Impostor) {
                sendImpostorTaskError(player);
                return;
            }
            FlipSwitches.run(player, block);
        } if (matchingMaterial(block, Colors.blockMaterial)) {
            if (a instanceof Impostor) {
                sendImpostorTaskError(player);
                return;
            }
            Colors.run(player, block);
        } if (matchingMaterial(block, PushButton.blockMaterial)) {
            if (a instanceof Impostor) {
                sendImpostorTaskError(player);
                return;
            }
            PushButton.run(player, block);
        } if (matchingMaterial(block, EmergencyMeeting.blockMaterial)) {
            if ( playerIsDead(a) ) { return; }
            EmergencyMeeting.run(player, block, false);
        } if (matchingMaterial(block, Vent.blockMaterial)) {            
            if ( a instanceof Crewmate ) {
                sendCrewmateVentError(player);
                return;
            }
            if ( playerIsDead(a) ) { return; }
            Vent.run(player, block);
        } if (matchingMaterial(block, Material.PLAYER_HEAD)) {
            if (!Game.checkIfBody(block)) {
                sendBenignReportError(player);
                return;
            }
            if ( playerIsDead(a) ) {
                sendGhostReportError(player);
                return;
            }
            if (a instanceof Impostor) {
                sendImpostorReportError(player);
                return;
            }
            Game.cleanUpBody(block);
            EmergencyMeeting.run(player, block, true);
        }
    }

    private void sendBenignReportError(Player player) {
        Broadcasting.sendError(player, "I'm not a dead body! I'm just a player head!");
    }

    private void sendGhostReportError(Player player) {
        Broadcasting.sendError(player, "You can't report bodies! You are a ghost!");
    }
    
    private void sendImpostorReportError(Player player) {
        Broadcasting.sendError(player, "You can't report bodies! You are an impostor!");
    }

    private void sendCrewmateVentError(Player player) {
        Broadcasting.sendError(player, "You can't vent! You are a crewmate!");
    }

    private void sendImpostorTaskError(Player player) {
        Broadcasting.sendError(player, "You can't do tasks! You are an impostor!");
    }

    private boolean playerIsDead(AmongUsPlayer a) {
        return !a.isALive();
    }

    private boolean matchingMaterial(Block block, Material material) {
        return block.getType().equals(material);
    }
}
