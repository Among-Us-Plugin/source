package io.papermc.aup.interactions;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Vent {

    private static int ventCooldownCounter = 0;
    
    public static void run(Player player, Block block) {
        if ( !(ventCooldownCounter <= 0) ) {
            Broadcasting.sendError(player, "Vent Cooldown: " + ventCooldownCounter + " seconds left");
            return;
        }
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        enterVent(player, block);
        impostor.startVenting();
        startVentCooldown();
    }

    public static void handleSneak() {
        for ( AmongUsPlayer a : Game.amongUsPlayers ) {
            if ( a instanceof Impostor ) {
                Impostor impostor = (Impostor)a;
                if ( impostor.isVenting() ) {
                    exitVent(a);
                    impostor.stopVenting();
                }
            }
        }
    }

    private static void startVentCooldown() {
        ventCooldownCounter = Game.ventCooldownInSeconds;
        // Bossbar?
        // add impostor to bossbar
        new BukkitRunnable() {
            @Override
            public void run() {
                ventCooldownCounter -= 1;
                // update bossbars
                if (ventCooldownCounter <= 0) {
                    // remove bossbar
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    // remove bossbar
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private static void exitVent(AmongUsPlayer a) {
        Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
        p.setGameMode(GameMode.SURVIVAL);
        spawnVentEffect(p.getLocation());
    }

    private static void enterVent(Player player, Block block) {
        Location location = player.getLocation();
        spawnVentEffect(location);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(block.getLocation());
    }

    private static void spawnVentEffect(Location location) {
        location.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_LARGE, location, 1);
    }

}
