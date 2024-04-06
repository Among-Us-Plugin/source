package io.papermc.aup.listeners;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class EmergencyMeetingStarter implements Listener {

    // Start the Emergency Meeting
    @EventHandler
    public void onDamage(BlockDamageEvent event) {

        Block block = event.getBlock();

        if(block.getType() != Material.PLAYER_HEAD) {
            return;
        }

        double i = 0;
        double radius = 5; // in Minecraft block

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = block.getX() + radius * Math.cos(angle);
            double newZ = block.getZ() + radius * Math.sin(angle);

            // Returns a value between -180 and 180, so the player faces towards the center
            float newYaw = (float)Math.toDegrees(Math.atan2(block.getZ() - newZ, block.getX() - newX)) - 90;
            if (newYaw < 0) {
                newYaw += 360;
            }

            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.teleport(new Location(player.getWorld(), newX, block.getY(), newZ, newYaw, 0));

            i ++;
        }
    }
}

