package io.papermc.aup.tasks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;

public class EmergencyMeeting {

    public static void run(Block centreBlock) {

        double i = 0;
        double radius = 5; // in blocks

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = centreBlock.getX() + radius * Math.cos(angle);
            double newZ = centreBlock.getZ() + radius * Math.sin(angle);

            // Returns a value between -180 and 180, so the player faces towards the center
            float newYaw = (float)Math.toDegrees(Math.atan2(centreBlock.getZ() - newZ, centreBlock.getX() - newX)) - 90;
            if (newYaw < 0) {
                newYaw += 360;
            }

            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.teleport(new Location(player.getWorld(), newX, centreBlock.getY(), newZ, newYaw, 0));

            i++;
        }
    }
    
}
