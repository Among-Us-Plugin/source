package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Game {

    public static boolean gameRunning = false;
    public static boolean emergencyMeetingInProgress = false;
    public static int discussionPeriodDurationInSeconds = 5;
    public static int meetingDurationInSeconds = 10;
    public static int meetingCooldownInSeconds = meetingDurationInSeconds + 10;
    public static int ventCooldownInSeconds = 15;
    public static int killCooldownInSeconds = 10;

    public static AmongUsPlayer[] amongUsPlayers;
    
    public static Sound alertSound = Sound.BLOCK_NOTE_BLOCK_BIT;
    public static Sound errorSound = Sound.BLOCK_ANVIL_LAND;
    public static Sound taskCompletionSound = Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
    public static Sound meetingStartSound = Sound.BLOCK_NOTE_BLOCK_HARP;
    public static Sound deathSound = Sound.BLOCK_END_PORTAL_SPAWN;
    public static Sound gameEndSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
    
    public static int numImpostors = 1;

    public static BossBar crewmatesBossBar;
    public static BossBar impostorsBossBar;
    public static BossBar taskBossBar;
    public static int taskBossBarProgressAsPercentage;

    public static BossBar meetingBossBar;

    public static BossBar meetingCooldownBossBar;
    public static BossBar ventCooldownBossBar;
    public static BossBar killCooldownBossBar;

    private static int taskBossBarIncrementPercentage = 20;

    public static void initializeBossBars() {
        Game.crewmatesBossBar = Bukkit.createBossBar(ChatColor.GREEN + "You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        Game.impostorsBossBar = Bukkit.createBossBar(ChatColor.RED + "You are an Impostor!", BarColor.RED, BarStyle.SOLID);
        Game.taskBossBar = Bukkit.createBossBar(ChatColor.BLUE + "Crewmate Task Progress: 0%", BarColor.BLUE, BarStyle.SEGMENTED_20);
        Game.taskBossBar.setProgress(0.0);
        taskBossBarProgressAsPercentage = 0;
    }

    public static void initializeKillCooldownBossBar() {
        killCooldownBossBar = Bukkit.createBossBar(ChatColor.RED + "Kill Cooldown: " + killCooldownInSeconds, BarColor.RED, BarStyle.SOLID);
    }

    public static void initializeVentCooldownBossBar() {
        ventCooldownBossBar = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE + "Vent Cooldown: " + ventCooldownInSeconds, BarColor.PINK, BarStyle.SOLID);
    }

    public static void initializeMeetingCooldownBossBar() {
        meetingCooldownBossBar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + "Meeting Cooldown: " + meetingCooldownInSeconds, BarColor.PURPLE, BarStyle.SOLID);
    }

    public static void initializeMeetingBossBar() {
        meetingBossBar = Bukkit.createBossBar(ChatColor.YELLOW + "Emergency Meeting: ", BarColor.YELLOW, BarStyle.SOLID);
    }

    public static void killPlayer(Entity entity) {
        if (entity instanceof Player) {
            if (!gameRunning) { return; }
            Player p = (Player) entity;
            killPlayer(p);
        }
    }

    public static void killPlayer(Player player) {
        if (!gameRunning) { return; }
        player.setGameMode(GameMode.SPECTATOR);
        spawnDeathParticleEffect(player);
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        a.kill();
        checkAmongUsPlayers();
    }

    private static void spawnDeathParticleEffect(Player player) {
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(org.bukkit.Particle.CRIT, loc, 500);
    }

    public static void endGame() {
        gameRunning = false;
        resetGameModes();
        Broadcasting.sendSoundToAllPlayers(gameEndSound);
    }

    public static void resetGameModes() {
        for (AmongUsPlayer a : amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    public static void closeAllInventories() {
        for (Player p : Bukkit.getOnlinePlayers() ) {
            p.closeInventory();
        }
    }

    // Crewmates win -> end game & send appropriate titles
    public static void crewmatesWin(String reason) {
        endGame();
        Broadcasting.sendCrewmatesWin(reason);
        return;
    }

    // Impostors win -> end game & send appropriate titles
    public static void impostorsWin(String reason) {
        endGame();
        Broadcasting.sendImpostorsWin(reason);
        return;
    }

    public static void handleTaskCompletion(HumanEntity entity) {

        Broadcasting.sendTaskCompletionSound(entity);

        if (taskBossBar == null) { return; }
        taskBossBarProgressAsPercentage += taskBossBarIncrementPercentage;
        double updatedProgressAsDouble = 0.01 * taskBossBarProgressAsPercentage;
        taskBossBar.setProgress(updatedProgressAsDouble);
        taskBossBar.setTitle(ChatColor.BLUE + "Crewmate Task Progress: " + taskBossBarProgressAsPercentage + "%");
    }
    
    // Check whether any AmongUsPlayers are left, end game if appropriate
    public static void checkAmongUsPlayers() {
        checkCrewmates();
        checkImpostors();
    }

    // Check whether any crewmates are left, end game if appropriate
    public static void checkCrewmates() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Crewmate && a.isALive()) {
                return;
            }
        }
        // There are no crewmates, end the game
        impostorsWin("There are no more crewmates!");
    }

    // Check whether any impostors are left, end game if appropriate
    public static void checkImpostors() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }
        // There are no impostors, end the game
        crewmatesWin("There are no more impostors!");
    }
}
