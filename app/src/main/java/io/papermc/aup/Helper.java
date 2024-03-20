package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;

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

}
