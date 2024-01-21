package io.papermc.aup.classes;

import org.bukkit.entity.Player;

public class AmongUsPlayer {

    private Player player;
    private boolean isAlive;

    public AmongUsPlayer(Player player) {
        isAlive = true;
    }

    public void death() {
        isAlive = false;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isALive() {
        return isAlive;
    }

}
