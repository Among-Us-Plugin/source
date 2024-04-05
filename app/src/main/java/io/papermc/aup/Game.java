package io.papermc.aup;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
    
    // Defines the number of impostors for the game
    public static int numImpostors = 1;

    // BossBars are instantiated in GameLogic
    public static BossBar crewmatesBossBar;
    public static BossBar impostorsBossBar;
    public static BossBar taskBossBar;

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
    public static void crewmatesWin() {
        endGame();
        Broadcasting.sendCrewmatesWin();
        return;
    }

    // Impostors win -> end game & send appropriate titles
    public static void impostorsWin() {
        endGame();
        Broadcasting.sendImpostorsWin();
        return;
    }

    public static void increaseTaskProgress() {
        if (taskBossBar == null) { return; }
        double currentProgress = taskBossBar.getProgress();
        double updatedProgress = currentProgress + 0.1;
        taskBossBar.setProgress(updatedProgress);
        taskBossBar.setTitle(ChatColor.BLUE + "Crewmate Task Progress: " + (int)(updatedProgress * 100) + "%");
    }
    
    // Check whether any AmongUsPlayers are left, end game if appropriate
    public static void checkAmongUsPlayers() {
        checkCrewmates();
        checkImpostors();
    }

    // Check whether any crewmates are left, end game if appropriate
    private static void checkCrewmates() {
        // If there are no crewmates left, end the game
        // If there is an crewmate, do nothing
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Crewmate && a.isALive()) {
                return;
            }
        }
        // There are no crewmates, end the game
        impostorsWin();
    }

    // Check whether any impostors are left, end game if appropriate
    private static void checkImpostors() {
        // If there are no impostors left, end the game
        // If there is an impostor, do nothing
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }
        // There are no impostors, end the game
        crewmatesWin();
    }
}
