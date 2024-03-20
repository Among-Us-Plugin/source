package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Helper {

    public static boolean gameRunning = false;
    public static AmongUsPlayer[] amongUsPlayers;

    // In the array, set an AmongUsPlayer's life status
    public static void death(AmongUsPlayer p) {
        for (int i = 0; i < amongUsPlayers.length; i++) {
            if (amongUsPlayers[i] == p) {
                amongUsPlayers[i].death();
            }
        }
        return;
    }
    public static void death(String name) {
        death(getAmongUsPlayerByDisplayName(name));
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
            if (a.getPlayerName().equals(displayName)) {
                return a;
            }
        }
        return null;
    }

    // Crewmates win -> send appropriate messages
    public static void crewmatesWin() {
        for (AmongUsPlayer a : Helper.amongUsPlayers) {
            Player p = Helper.getPlayerByDisplayName(a.getPlayerName());
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + "The crewmates won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.RED + "DEFEAT", ChatColor.RED + "The crewmates won!", 10, 40, 10);
            }
        }
        return;
    }

    // Impostors win -> send appropriate messages
    static void impostorsWin() {
        for (AmongUsPlayer a : Helper.amongUsPlayers) {
            Player p = Helper.getPlayerByDisplayName(a.getPlayerName());
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.GREEN + "DEFEAT", ChatColor.RED + "The impostors won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.RED + "VICTORY", ChatColor.GREEN + "The impostors won!", 10, 40, 10);
            }
        }
        return;
    }

}
