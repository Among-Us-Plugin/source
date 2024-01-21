package io.papermc.aup.classes;

import org.bukkit.entity.Player;

public class AmongUsPlayer {

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
