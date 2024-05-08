package io.papermc.aup.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class ImpostorKillHandler implements Listener {

    // Ensure that the impostor can kill with one click
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        Entity damagerEntity = event.getDamager();
        Entity victimEntity = event.getEntity();
        Player impostorPlayer = (Player) damagerEntity;

        if (!Game.gameRunning) { return; }
        if (!(damagerEntity instanceof Player)) { return; }
        if (!(victimEntity instanceof Player)) { return; }
            
        AmongUsPlayer damager = AmongUsPlayer.getAmongUsPlayerByDisplayName(damagerEntity.getName());
        AmongUsPlayer victim = AmongUsPlayer.getAmongUsPlayerByDisplayName(victimEntity.getName());

        if (damager instanceof Impostor) {
            Impostor i = (Impostor) damager;
            event.setCancelled(true);
            if (victim instanceof Impostor) {
                Broadcasting.sendError(impostorPlayer, "You cannot kill other impostors!");
                return;
            }
            if ( killCooldownIsActive(i) ) {
                Broadcasting.sendError(impostorPlayer, "Kill Cooldown: " + i.getKillCooldown() + " seconds left");
                return;
            }
            killVictimEntity(victimEntity);
            startKillCooldown(impostorPlayer, i);
        }
    }

    private boolean killCooldownIsActive(Impostor i) {
        return (i.getKillCooldown() > 0);
    }

    private static void startKillCooldown(Player player, Impostor i) {
        i.setKillCooldown(Game.killCooldownInSeconds);
        i.initializeKillCooldownBossBar();
        i.killCooldownBossBar.addPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                i.setKillCooldown(i.getKillCooldown() - 1);
                i.killCooldownBossBar.setTitle(ChatColor.RED + "Kill Cooldown: " + i.getKillCooldown());
                i.killCooldownBossBar.setProgress((float)i.getKillCooldown() / Game.killCooldownInSeconds);
                if (i.getKillCooldown() <= 0) {
                    i.killCooldownBossBar.removeAll();
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    i.setKillCooldown(0);
                    i.killCooldownBossBar.removeAll();
                    this.cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private void killVictimEntity(Entity victimEntity) {
        Player victimPlayer = (Player) victimEntity;
        Broadcasting.sendDeathSound(victimPlayer);
        Game.killPlayer(victimPlayer);
        Game.placeCorpse(victimPlayer);
    }
}
