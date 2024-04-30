package io.papermc.aup.interactions;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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

    public static Material blockMaterial = Material.IRON_TRAPDOOR;

    private static int ventCooldownCounter = 0;
    
    public static void run(Player player, Block block) {
        if ( ventCooldownIsActive() ) {
            Broadcasting.sendError(player, "Vent Cooldown: " + ventCooldownCounter + " seconds left");
            return;
        }
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        enterVent(player, block);
        impostor.startVenting();
        startVentCooldown(player);
    }

    private static boolean ventCooldownIsActive() {
        return (ventCooldownCounter > 0);
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

    private static void startVentCooldown(Player player) {
        ventCooldownCounter = Game.ventCooldownInSeconds;
        Game.initializeVentCooldownBossBar();
        Game.ventCooldownBossBar.addPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                ventCooldownCounter -= 1;
                Game.ventCooldownBossBar.setTitle(ChatColor.LIGHT_PURPLE + "Vent Cooldown: " + ventCooldownCounter);
                Game.ventCooldownBossBar.setProgress((float)ventCooldownCounter / Game.ventCooldownInSeconds);
                if (ventCooldownCounter <= 0) {
                    Game.ventCooldownBossBar.removeAll();
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    ventCooldownCounter = 0;
                    Game.ventCooldownBossBar.removeAll();
                    this.cancel();
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
