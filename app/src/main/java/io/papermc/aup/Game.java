package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class Game {

    public static boolean gameRunning = false;
    public static AmongUsPlayer[] amongUsPlayers;
    
    // Defines the number of impostors for the game
    public static int numImpostors = 1;

    // In the array, set an AmongUsPlayer's life status
    public static void kill(AmongUsPlayer p) {
        for (int i = 0; i < amongUsPlayers.length; i++) {
            if (amongUsPlayers[i] == p) {
                amongUsPlayers[i].kill();
            }
        }
        return;
    }
    public static void kill(String name) {
        kill(getAmongUsPlayerByDisplayName(name));
        return;
    }
    
    // Return Player object based on friendly username
    public static Player getPlayerByDisplayName(String displayName) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getDisplayName().equals(displayName)) {
                return p;
            }
        }
        return null;
    }

    // Return AmongUsPlayer object based on friendly username
    public static AmongUsPlayer getAmongUsPlayerByDisplayName(String displayName) {
        for (AmongUsPlayer a : amongUsPlayers) {
            if (a.getDisplayName().equals(displayName)) {
                return a;
            }
        }
        return null;
    }

    // Return Player object based on AmongUsPlayer object
    public static Player getPlayerByAmongPlayer(AmongUsPlayer a) {
        Player p = getPlayerByDisplayName(a.getDisplayName());
        return p;
    }

    // Crewmates win -> end game & send appropriate messages
    public static void crewmatesWin() {
        Game.gameRunning = false;
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = getPlayerByAmongPlayer(a);
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + "The crewmates won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.RED + "DEFEAT", ChatColor.RED + "The crewmates won!", 10, 40, 10);
            }
        }
        return;
    }

    // Impostors win -> end game & send appropriate messages
    public static void impostorsWin() {
        Game.gameRunning = false;
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = getPlayerByAmongPlayer(a);
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.RED + "DEFEAT", ChatColor.RED + "The impostors won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + "The impostors won!", 10, 40, 10);
            }
        }
        return;
    }

    // Check whether any crewmates are left, end game if appropriate
    public static void checkCrewmates() {

        // If there are no crewmates left, end the game
        // If there is an crewmate, do nothing
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Crewmate && a.isALive()) {
                return;
            }
        }
        // There are no crewmates, end the game
        impostorsWin();
        Component c = Component.text("Game Ended: There are no more crewmates!", NamedTextColor.GOLD);
        Bukkit.broadcast(c);
    }

    // Check whether any impostors are left, end game if appropriate
    public static void checkImpostors() {

        // If there are no impostors left, end the game
        // If there is an impostor, do nothing
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }
        // There are no impostors, end the game
        crewmatesWin();
        Component c = Component.text("Game Ended: There are no more impostors!", NamedTextColor.GOLD);
        Bukkit.broadcast(c);
    }

}
