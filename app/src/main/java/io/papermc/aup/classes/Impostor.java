package io.papermc.aup.classes;

import org.bukkit.entity.Player;

public class Impostor extends AmongUsPlayer {

    private boolean isVenting;
    private int ventCooldown;
    private int killCooldown;
    
    public Impostor(Player player) {
        super(player);
        isVenting = false;
        ventCooldown = 0;
        killCooldown = 0;
    }

    public void setVentCooldown(int v) {
        ventCooldown = v;
    }

    public void setKillCooldown(int k) {
        killCooldown = k;
    }
    
    public int getVentCooldown() {
        return ventCooldown;
    }

    public int getKillCooldown() {
        return killCooldown;
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
