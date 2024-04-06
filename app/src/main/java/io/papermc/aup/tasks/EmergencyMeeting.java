package io.papermc.aup.tasks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;

public class EmergencyMeeting {

    public static void run(Block centreBlock) {

        Game.emergencyMeetingInProgress = true;

        double i = 0;
        double radius = 5; // in blocks

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = centreBlock.getX() + radius * Math.cos(angle);
            double newZ = centreBlock.getZ() + radius * Math.sin(angle);
            float newYaw = getNewYaw(centreBlock, newX, newZ);
            
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.teleport(new Location(player.getWorld(), newX, centreBlock.getY(), newZ, newYaw, 0));
            
            i++;
        }
    }
    
    // Returns a value between -180 and 180, so the player faces towards the center
    private static float getNewYaw(Block centreBlock, double newX, double newZ) {
        float newYaw = (float)Math.toDegrees(Math.atan2(centreBlock.getZ() - newZ, centreBlock.getX() - newX)) - 90;
        if (newYaw < 0) {
            newYaw += 360;
        }
        return newYaw;
    }
}
