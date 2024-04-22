package io.papermc.aup.classes;

import org.bukkit.entity.Player;

public class Impostor extends AmongUsPlayer {

    private boolean isVenting;
    
    public Impostor(Player player) {
        super(player);
        isVenting = false;
    }

    public boolean isVenting() {
        return isVenting;
    }

    public void startVenting() {
        isVenting = true;
    }

    public void stopVenting() {
        isVenting = false;
    }

}
