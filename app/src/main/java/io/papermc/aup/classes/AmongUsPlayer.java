package io.papermc.aup.classes;

import org.bukkit.entity.Player;

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

}
