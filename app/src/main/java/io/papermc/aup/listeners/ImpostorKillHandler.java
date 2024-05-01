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

        Entity damager = event.getDamager();
        Entity victim = event.getEntity();
        Player impostorPlayer = (Player) damager;

        if (!Game.gameRunning) { return; }
        if (!(damager instanceof Player)) { return; }
            
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(damager.getName());
        if (isImpostor(damager, a)) {
            if ( killCooldownIsActive() ) {
                Broadcasting.sendError(impostorPlayer, "Kill Cooldown: " + killCooldownCounter + " seconds left");
                return;
            }
            event.setCancelled(true);
            killVictim(victim);
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
    }

    private boolean isImpostor(Entity damager, AmongUsPlayer a) {
        return a instanceof Impostor && a.getDisplayName().equals(damager.getName());
    }
}
