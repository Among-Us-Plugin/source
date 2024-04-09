package io.papermc.aup.tasks;

import io.papermc.aup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class EmergencyMeeting {
    public static String title = "Vote";
    public static HashMap<String, String> votes = new HashMap<String, String >(); // <Voter, Voted>


    public static void run(Block centreBlock) {
        Game.emergencyMeetingInProgress = true;
        relocatePlayers(centreBlock);
        Inventory votingMenu = getVotingMenu();
        populateVotingMenu(votingMenu);
        openVotingMenus(votingMenu);
        startMeetingTimer();
    }
        // region teleport players
        double radius = 5; // in blocks

        for (int i = 0; i < Game.amongUsPlayers.length; i++){
            AmongUsPlayer amongUsPlayer = Game.amongUsPlayers[i];


            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;

            double newX = centreBlock.getX() + playersRadius * Math.cos(angle);
            double newZ = centreBlock.getZ() + playersRadius * Math.sin(angle);
            float newYaw = getNewYaw(centreBlock, newX, newZ);
            
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);

            // player.teleport(new Location(player.getWorld(), newX, centreBlock.getY(), newZ, newYaw, 0));
            
            i++;
        }
        // endregion


        int invSize = ((Game.amongUsPlayers.length % 7) + 2) * 9;

        Inventory inv = Bukkit.createInventory(null, invSize, title);

        for (int i = 0; i < Game.amongUsPlayers.length; i++){
            AmongUsPlayer amongUsPlayer = Game.amongUsPlayers[i];

            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(player);
            meta.setDisplayName("§6" + amongUsPlayer.getDisplayName());
            skull.setItemMeta(meta);

            inv.setItem(getPlayerHeadSlotIndex(i), skull);

            i++;
        }
    }

        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.openInventory(inv);
        }

        Bukkit.getLogger().info("Waiting for 5 seconds");

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("Running the task");
            }
        }.runTaskLater(JavaPlugin.getPlugin(Main.class), 100);

        Bukkit.getLogger().info("5 seconds Finished, proceeding...");
    }

    public static void handleClick(InventoryClickEvent event) {
        ItemStack playerVoted = event.getCurrentItem();

        String voter = event.getWhoClicked().getName();
        String voted = playerVoted.getItemMeta().getDisplayName().substring(2);

        Bukkit.getLogger().info("Voter: " + voter +  "Voted: " + voted);
        votes.put(voter, voted);
    }

    private static int getPlayerHeadSlotIndex(int amongUsPlayerIndex){
        int numberOfRows = (amongUsPlayerIndex / 7) + 1;

        return ((amongUsPlayerIndex + 1) % 7) + (numberOfRows * 9);

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
