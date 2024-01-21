package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;

public class Helper {

    public static boolean gameRunning = false;
    public static AmongUsPlayer[] amongUsPlayers;
    
    // Return Player object based on friendly username
    public static Player getPlayerByDisplayName(String displayName) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getDisplayName().equals(displayName)) {
                return p;
            }
        }
        return null;
    }

}
