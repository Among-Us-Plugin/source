package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Game {

    public static boolean gameRunning = false;
    public static AmongUsPlayer[] amongUsPlayers;
    
    public static int numImpostors = 1;

    public static BossBar crewmatesBossBar;
    public static BossBar impostorsBossBar;
    public static BossBar taskBossBar;
    public static int taskBossBarProgressAsPercentage;

    public static void initializeBossBars() {
        Game.crewmatesBossBar = Bukkit.createBossBar(ChatColor.GREEN + "You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        Game.impostorsBossBar = Bukkit.createBossBar(ChatColor.RED + "You are an Impostor!", BarColor.RED, BarStyle.SOLID);
        Game.taskBossBar = Bukkit.createBossBar(ChatColor.BLUE + "Crewmate Task Progress: 0%", BarColor.BLUE, BarStyle.SEGMENTED_10);
        Game.taskBossBar.setProgress(0.0);
        taskBossBarProgressAsPercentage = 0;
    }

    public static void killAmongUsPlayer(AmongUsPlayer a) {
        for (int i = 0; i < amongUsPlayers.length; i++) {
            if (amongUsPlayers[i] == a) {
                amongUsPlayers[i].kill();
            }
        }
        return;
    }

    public static void killAmongUsPlayer(String displayName) {
        killAmongUsPlayer(AmongUsPlayer.getAmongUsPlayerByDisplayName(displayName));
        return;
    }

    public static void killPlayer(Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            p.damage(p.getHealth());
            if (!gameRunning) { return; }
            p.setGameMode(GameMode.SPECTATOR);
        }
    }

    public static void endGame() {
        gameRunning = false;
        resetGameModes();
    }

    public static void resetGameModes() {
        for (AmongUsPlayer a : amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
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

    public static void increaseTaskProgress() {
        if (taskBossBar == null) { return; }
        taskBossBarProgressAsPercentage += 10;
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
    private static void checkCrewmates() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Crewmate && a.isALive()) {
                return;
            }
        }
        // There are no crewmates, end the game
        impostorsWin("There are no more crewmates!");
    }

    // Check whether any impostors are left, end game if appropriate
    private static void checkImpostors() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }
        // There are no impostors, end the game
        crewmatesWin("There are no more impostors!");
    }
}
