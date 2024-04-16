package io.papermc.aup.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Vote;
import net.kyori.adventure.text.Component;

@SuppressWarnings("deprecation")
public class EmergencyMeeting {

    public static String title = "Vote out the impostor!";

    public static ArrayList<Vote> votes = new ArrayList<Vote>();

    private static int votingMenuSize = (((Game.amongUsPlayers.length - 1) / 9) + 1) * 9;
    private static double playersRadius = 5;
    private static int meetingDurationCounter = Game.meetingDurationInSeconds;

    public static void run(Block centreBlock) {
        Game.emergencyMeetingInProgress = true;
        relocatePlayers(centreBlock);
        Inventory votingMenu = getVotingMenu();
        populateVotingMenu(votingMenu);
        openVotingMenus(votingMenu);
        startMeetingTimer();
        Game.initializeMeetingBossBars();
        addPlayersToMeetingBossBars();
    }

    private static void startMeetingTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                meetingDurationCounter--;
                updateMeetingBossBars();
                if (meetingDurationCounter <= 0) {
                    Game.emergencyMeetingInProgress = false;
                    meetingDurationCounter = Game.meetingDurationInSeconds;
                    Game.meetingBossBar.removeAll();
                    handleVotes();
                    this.cancel();
                }
            }

            private void updateMeetingBossBars() {
                Game.meetingBossBar.setTitle("Emergency Meeting: " + meetingDurationCounter);
                Game.meetingBossBar.setProgress((float)meetingDurationCounter / Game.meetingDurationInSeconds);
            }
            
            // 20 ticks = 1 second, under normal circumstances
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }
    
    public static void handleClick(InventoryClickEvent event) {
        
        if (!validIndex(event.getSlot())) { return; }
        ItemStack playerHeadVoted = event.getCurrentItem();
        if ( playerHeadVoted == null ) { return; }
        if (!checkIfPlayerHead(playerHeadVoted)) { return; }
        
        Player whoClicked = (Player)event.getWhoClicked();
        AmongUsPlayer voter = AmongUsPlayer.getAmongUsPlayerByDisplayName(whoClicked.getDisplayName());
        AmongUsPlayer vote = AmongUsPlayer.getAmongUsPlayerByDisplayName(getDisplayNameFromHead(playerHeadVoted));

        registerVote(voter, vote);
        
        InventoryView v = event.getView();
        v.close();
    }

    private static void registerVote(AmongUsPlayer voter, AmongUsPlayer vote) {
        Vote v = new Vote(voter, vote);
        votes.add(v);
    }

    private static String getDisplayNameFromHead(ItemStack playerHeadVoted) {
        return playerHeadVoted.getItemMeta().getDisplayName().substring(2);
    }

    private static void handleVotes() {
        for (Vote v : votes) {
            Component c = Component.text("" + v.getVoter() + " voted " + v.getRecipient() + "!");
            Bukkit.broadcast(c);
        }

        AmongUsPlayer mostFrequent = getMostVotedAmongUsPlayer();
        if (mostFrequent == null) {
            Component e = Component.text("No one was ejected.");
            Bukkit.broadcast(e);
            return;
        }

        Component e = Component.text("We are ejecting " + mostFrequent.getDisplayName());
        Bukkit.broadcast(e);
        votes.clear();
    }

    private static AmongUsPlayer getMostVotedAmongUsPlayer() {
        Map<AmongUsPlayer, Integer> countMap = new HashMap<>();
        for (Vote v : votes) {
            AmongUsPlayer a = v.getRecipient();
            countMap.put(a, countMap.getOrDefault(a, 0) + 1);
        }

        AmongUsPlayer mostRepeated = null;
        int maxCount = 0;
        for (Map.Entry<AmongUsPlayer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostRepeated = entry.getKey();
                maxCount = entry.getValue();
            } else if (entry.getValue() == maxCount) {
                // If there is a tie, return null
                return null;
            }
        }
        return mostRepeated;
    }
    
    private static void addPlayersToMeetingBossBars() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            Game.meetingBossBar.addPlayer(p);
        }
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
        Inventory inv = Bukkit.createInventory(null, votingMenuSize, title);
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

    private static boolean checkIfPlayerHead(ItemStack s) {
        return s.getType().equals(Material.PLAYER_HEAD);
    }

    private static boolean validIndex(int index) {
        return (index >= 0 && index < votingMenuSize);
    }
}