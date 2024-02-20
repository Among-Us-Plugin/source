package io.papermc.aup.classes;

import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class AmongUsPlayer {

    // Stores a String playerName, NOT a Player object
    // Why? Player objects store everything about a player and are subject to change; location, ping, etc.
    private String playerName;
    private boolean isAlive;

    public AmongUsPlayer(Player player) {
        playerName = player.getDisplayName();
        isAlive = true;
    }

    public void death() {
        isAlive = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isALive() {
        return isAlive;
    }

    public String toString() {
        return playerName;
    }

}
