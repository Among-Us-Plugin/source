package io.papermc.aup.interactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;
import io.papermc.aup.classes.Vote;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class EmergencyMeeting {

    public static String inventoryTitle = "Vote out the impostor!";
    public static Material blockMaterial = Game.emergencyMeetingMaterial;

    public static ArrayList<Vote> votes = new ArrayList<Vote>();

    private static int inventorySize = (((Game.amongUsPlayers.length - 1) / 9) + 1) * 9;
    private static double meetingPlayersRadius = 5;
    private static double bodyPlayersRadius = 2;
    private static int meetingDurationCounter = Game.meetingDurationInSeconds;
    private static int discussionPeriodDurationCounter = Game.discussionPeriodDurationInSeconds;

    private static int meetingCooldownCounter = 0;

    public static void run(Player player, Block centreBlock, boolean isBodyReport) {
        if ( !isBodyReport && meetingCooldownIsActive() ) {
            Broadcasting.sendError(player, "Meeting Cooldown: " + meetingCooldownCounter + " seconds left");
            return;
        }
        Game.emergencyMeetingInProgress = true;
        if (!isBodyReport) {
            Broadcasting.broadcastSignedMessage( player.getName() + " has initiated an emergency meeting!", NamedTextColor.LIGHT_PURPLE);
            startMeetingCooldown();
            relocatePlayers(centreBlock, meetingPlayersRadius);
        } else {
            Broadcasting.broadcastSignedMessage( player.getName() + " has found a dead body!", NamedTextColor.LIGHT_PURPLE);
            relocatePlayers(centreBlock, bodyPlayersRadius);
        }
        Game.initializeMeetingBossBar();
        Game.cleanUpAllBodies();
        addPlayersToMeetingBossBars();
        Broadcasting.sendSoundToAllPlayers(Game.meetingStartSound);
        startMeetingTimer();
        runMeeting();
    }

    private static void runMeeting() {
        discussionPeriodDurationCounter = Game.discussionPeriodDurationInSeconds;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Game.gameRunning) { 
                    discussionPeriodDurationCounter = Game.discussionPeriodDurationInSeconds;
                    this.cancel();
                }
                discussionPeriodDurationCounter--;
                if (discussionPeriodDurationCounter <= 0) {
                    discussionPeriodDurationCounter = Game.discussionPeriodDurationInSeconds;
                    startVoting();
                    this.cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private static void startVoting() {
        Inventory votingMenu = getVotingMenu();
        populateVotingMenu(votingMenu);
        openVotingMenus(votingMenu);
    }

    private static boolean meetingCooldownIsActive() {
        return meetingCooldownCounter > 0;
    }

    private static void startMeetingCooldown() {
        meetingCooldownCounter = Game.meetingCooldownInSeconds;
        Game.initializeMeetingCooldownBossBar();
        addPlayersToMeetingCooldownBossBar();
        new BukkitRunnable() {
            @Override
            public void run() {
                meetingCooldownCounter -= 1;
                Game.meetingCooldownBossBar.setTitle(ChatColor.DARK_PURPLE + "Meeting Cooldown: " + meetingCooldownCounter);
                Game.meetingCooldownBossBar.setProgress((float)meetingCooldownCounter / Game.meetingCooldownInSeconds);
                if (meetingCooldownCounter <= 0) {
                    Game.meetingCooldownBossBar.removeAll();
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    meetingCooldownCounter = 0;
                    Game.meetingCooldownBossBar.removeAll();
                    this.cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private static void startMeetingTimer() {
        meetingDurationCounter = Game.meetingDurationInSeconds;
        new BukkitRunnable() {
            @Override
            public void run() {
                meetingDurationCounter--;
                updateMeetingBossBars();
                if ( !Game.gameRunning ) {
                    votes.clear();
                    cleanUp();
                    this.cancel();
                }
                if (meetingDurationCounter <= 0) {
                    cleanUp();
                    handleVotes();
                    this.cancel();
                }
            }

            private void cleanUp() {
                Game.emergencyMeetingInProgress = false;
                meetingDurationCounter = Game.meetingDurationInSeconds;
                Game.meetingBossBar.removeAll();
            }

            private void updateMeetingBossBars() {
                Game.meetingBossBar.setTitle(ChatColor.YELLOW + "Emergency Meeting: " + meetingDurationCounter);
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
        
        AmongUsPlayer voter = getVoter(event);

        if ( !voter.isALive() ) { return; }

        AmongUsPlayer recipient = getRecipient(playerHeadVoted);

        registerVote(voter, recipient);
        
        InventoryView v = event.getView();
        v.close();
    }

    private static AmongUsPlayer getRecipient(ItemStack playerHeadVoted) {
        AmongUsPlayer recipient = AmongUsPlayer.getAmongUsPlayerByDisplayName(getDisplayNameFromHead(playerHeadVoted));
        return recipient;
    }

    private static AmongUsPlayer getVoter(InventoryClickEvent event) {
        Player whoClicked = (Player)event.getWhoClicked();
        AmongUsPlayer voter = AmongUsPlayer.getAmongUsPlayerByDisplayName(whoClicked.getDisplayName());
        return voter;
    }

    private static void registerVote(AmongUsPlayer voter, AmongUsPlayer recipient) {
        Player voterAsPlayer = AmongUsPlayer.getPlayerByAmongUsPlayer(voter);
        Broadcasting.sendSignedMessageToPlayer(voterAsPlayer, "You have voted for " + recipient.getDisplayName() + ".", NamedTextColor.GOLD);
        Broadcasting.broadcastSignedMessage(voterAsPlayer.getName() + " has voted.", NamedTextColor.GOLD);

        Vote v = new Vote(voter, recipient);
        votes.add(v);
    }

    private static String getDisplayNameFromHead(ItemStack playerHeadVoted) {
        return playerHeadVoted.getItemMeta().getDisplayName().substring(2);
    }

    private static void handleVotes() {

        Game.closeAllInventories();

        AmongUsPlayer mostVoted = getMostVotedAmongUsPlayer();
        if (mostVoted == null) {
            Broadcasting.broadcastSignedMessage("No one was ejected.", NamedTextColor.LIGHT_PURPLE);
            votes.clear();
            return;
        }
        Broadcasting.broadcastSignedMessage(mostVoted.getDisplayName() + " was ejected.", NamedTextColor.LIGHT_PURPLE);
        votes.clear();

        Player playerToBeEjected = Bukkit.getPlayer(mostVoted.getDisplayName());
        Broadcasting.sendDeathSound(playerToBeEjected);
        Game.killPlayer(playerToBeEjected);
    }

    private static AmongUsPlayer getMostVotedAmongUsPlayer() {
        Map<AmongUsPlayer, Integer> countMap = new HashMap<>();
        for (Vote v : votes) {
            AmongUsPlayer a = v.getRecipient();
            countMap.put(a, countMap.getOrDefault(a, 0) + 1);
        }

        AmongUsPlayer mostVoted = null;
        int maxCount = 0;
        for (Map.Entry<AmongUsPlayer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostVoted = entry.getKey();
                maxCount = entry.getValue();
            } else if (entry.getValue() == maxCount) {
                // If there is a tie, return null
                return null;
            }
        }
        return mostVoted;
    }

    private static void addPlayersToMeetingCooldownBossBar() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            Game.meetingCooldownBossBar.addPlayer(p);
        }
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
    
    private static void relocatePlayers(Block centreBlock, double radius) {
        int i = 0;
        for (AmongUsPlayer amongUsPlayer : Game.amongUsPlayers) {
            // Evenly spaces the players around a circle
            double angle = 2 * Math.PI * i / Game.amongUsPlayers.length;
            
            double newX = centreBlock.getX() + radius * Math.cos(angle);
            double newZ = centreBlock.getZ() + radius * Math.sin(angle);
            float newYaw = getNewYaw(centreBlock, newX, newZ);
            
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayer);
            player.teleport(new Location(player.getWorld(), newX, centreBlock.getY() + 2, newZ, newYaw, 0));
            
            i++;

            if ( amongUsPlayer instanceof Impostor ) {
                Impostor impostor = (Impostor) amongUsPlayer;
                impostor.stopVenting();
                player.setGameMode(GameMode.SURVIVAL);
            }
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
        Inventory inv = Bukkit.createInventory(null, inventorySize, inventoryTitle);
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
        return (index >= 0 && index < inventorySize);
    }
}