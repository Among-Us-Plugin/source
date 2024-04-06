package io.papermc.aup.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import io.papermc.aup.Game;
import io.papermc.aup.tasks.Colors;
import io.papermc.aup.tasks.EmergencyMeeting;
import io.papermc.aup.tasks.FlipSwitches;

public class TaskInteractionHandler implements Listener {
    
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
            Block block = event.getClickedBlock();
            EmergencyMeeting.run(block);
        }
    }
}
