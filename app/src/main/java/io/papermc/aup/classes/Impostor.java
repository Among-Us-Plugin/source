package io.papermc.aup.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class Impostor extends AmongUsPlayer {

    private boolean isVenting;
    
    private int ventCooldown;
    private int killCooldown;

    public BossBar ventCooldownBossBar;
    public BossBar killCooldownBossBar;

    private Block vent;
    
    public Impostor(Player player) {
        super(player);
        isVenting = false;
        ventCooldown = 0;
        killCooldown = 0;
    }

    public void initializeKillCooldownBossBar() {
        killCooldownBossBar = Bukkit.createBossBar(ChatColor.RED + "Kill Cooldown: " + Game.killCooldownInSeconds, BarColor.RED, BarStyle.SOLID);
    }

    public void initializeVentCooldownBossBar() {
        ventCooldownBossBar = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE + "Vent Cooldown: " + Game.ventCooldownInSeconds, BarColor.PINK, BarStyle.SOLID);
    }

    public void setVentBlock(Block block) {
        vent = block;
    }

    public Block getVentBlock() {
        return vent;
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
