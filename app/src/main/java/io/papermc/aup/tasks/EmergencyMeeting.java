package io.papermc.aup.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;

@SuppressWarnings("deprecation")
public class EmergencyMeeting {

    private static int votingMenuSize = 18;
    private static double playersRadius = 5;
    private static int meetingDurationInSeconds = 10;

    public static void run(Block centreBlock) {
        Game.emergencyMeetingInProgress = true;
        relocatePlayers(centreBlock);
        Inventory votingMenu = getVotingMenu();
        populateVotingMenu(votingMenu);
        openVotingMenus(votingMenu);
        startMeetingTimer();
    }

    private static void startMeetingTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                meetingDurationInSeconds--;
                if (meetingDurationInSeconds <= 0) {
                    Game.emergencyMeetingInProgress = false;
                }
            }
            
        // 20 ticks = 1 second, under normal circumstances
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private static void openVotingMenus(Inventory votingMenu) {
        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.openInventory(votingMenu);
        }
    }

    private static void relocatePlayers(Block centreBlock) {
        int i = 0;
        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = centreBlock.getX() + playersRadius * Math.cos(angle);
            double newZ = centreBlock.getZ() + playersRadius * Math.sin(angle);
            float newYaw = getNewYaw(centreBlock, newX, newZ);
            
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.teleport(new Location(player.getWorld(), newX, centreBlock.getY(), newZ, newYaw, 0));
            
            i++;
        }
    }

    private static void populateVotingMenu(Inventory votingMenu) {
        int index = 0;
        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName("§6" + amongUsPlayer.getDisplayName());
            skull.setItemMeta(meta);
            votingMenu.setItem(index, skull);
            index++;
        }
    }

    private static Inventory getVotingMenu() {
        String title = "Vote";
        Inventory inv = Bukkit.createInventory(null, votingMenuSize, title);
        Bukkit.getLogger().info("Meeting Started by someone");
        return inv;
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
