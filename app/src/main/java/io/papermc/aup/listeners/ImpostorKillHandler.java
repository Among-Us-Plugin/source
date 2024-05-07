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

    private static int killCooldownCounter = 0;

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

        if (isImpostor(damagerEntity, damager)) {
            event.setCancelled(true);
            if (victim instanceof Impostor) {
                Broadcasting.sendError(impostorPlayer, "You cannot kill other impostors!");
                return;
            }
            if ( killCooldownIsActive() ) {
                Broadcasting.sendError(impostorPlayer, "Kill Cooldown: " + killCooldownCounter + " seconds left");
                return;
            }
            killVictim(victimEntity);
            startKillCooldown(impostorPlayer);
        }
    }

    private boolean killCooldownIsActive() {
        return killCooldownCounter > 0;
    }

    private static void startKillCooldown(Player player) {
        killCooldownCounter = Game.killCooldownInSeconds;
        Game.initializeKillCooldownBossBar();
        Game.killCooldownBossBar.addPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                killCooldownCounter -= 1;
                Game.killCooldownBossBar.setTitle(ChatColor.RED + "Kill Cooldown: " + killCooldownCounter);
                Game.killCooldownBossBar.setProgress((float)killCooldownCounter / Game.killCooldownInSeconds);
                if (killCooldownCounter <= 0) {
                    Game.killCooldownBossBar.removeAll();
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    killCooldownCounter = 0;
                    Game.killCooldownBossBar.removeAll();
                    this.cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private void killVictim(Entity victim) {
        Broadcasting.sendDeathSound((Player) victim);
        Game.killPlayer(victim);
        Game.placeCorpse((Player)victim);
    }

    private boolean isImpostor(Entity damager, AmongUsPlayer a) {
        return a instanceof Impostor && a.getDisplayName().equals(damager.getName());
    }
}
