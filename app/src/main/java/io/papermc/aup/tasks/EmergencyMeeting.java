package io.papermc.aup.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class EmergencyMeeting {
    private static Material playerHeadMaterial = Material.PLAYER_HEAD;

    private static ItemStack playerHeadStack = new ItemStack(playerHeadMaterial);

    public static void run(Block centreBlock) {

        Game.emergencyMeetingInProgress = true;

        int i = 0;

        // region teleport players

        double radius = 5; // in blocks

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = centreBlock.getX() + radius * Math.cos(angle);
            double newZ = centreBlock.getZ() + radius * Math.sin(angle);
            float newYaw = getNewYaw(centreBlock, newX, newZ);
            
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
//            Disabled per dev purposes
//            player.teleport(new Location(player.getWorld(), newX, centreBlock.getY(), newZ, newYaw, 0));
            
            i++;
        }
        // endregion


        String title = "Vote";
        int invSize = 54;

        Inventory inv = Bukkit.createInventory(null, invSize, title);

        Bukkit.getLogger().info("Meeting Started by someone");

        ItemMeta correctItemMeta = playerHeadStack.getItemMeta();
        correctItemMeta.setDisplayName("§aON");

        playerHeadStack.setItemMeta(correctItemMeta);



        i = 0;

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName("§6" + amongUsPlayer.getDisplayName());
            skull.setItemMeta(meta);

            inv.setItem(i, skull);

            i++;
        }

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.openInventory(inv);
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
