package io.papermc.aup.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class AmongUsPlayer {

    // Stores a String displayName, NOT a Player object
    // Why? Player objects store everything about a player and are subject to change; location, ping, etc.
    private String displayName;
    private boolean isAlive;

    public AmongUsPlayer(Player player) {
        displayName = player.getDisplayName();
        isAlive = true;
    }

    public void kill() {
        isAlive = false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isALive() {
        return isAlive;
    }

    public String toString() {
        return displayName;
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

    // Return an in-game AmongUsPlayer object based on friendly username
    public static AmongUsPlayer getAmongUsPlayerByDisplayName(String displayName) {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
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

}
