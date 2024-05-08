package io.papermc.aup.interactions;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Random;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.Main;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Vent {

    public static Material blockMaterial = Material.IRON_TRAPDOOR;
    
    public static void run(Player player, Block block) {

        Impostor i = (Impostor) AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());

        if ( ventCooldownIsActive(i) ) {
            Broadcasting.sendError(player, "Vent Cooldown: " + i.getVentCooldown() + " seconds left");
            return;
        }
        i.setVentBlock(block);
        enterVent(player, block);
        i.startVenting();
        startVentCooldown(player, i);
    }

    private static boolean ventCooldownIsActive(Impostor i) {
        return (i.getVentCooldown() > 0);
    }

    public static boolean handleMove(Player p, Impostor i) {
        if (Game.vents.size() <= 1) { return false; }
        Block vent;
        do {
            Random rand = new Random();
            int randomIndex = rand.nextInt(Game.vents.size());
            vent = Game.vents.get(randomIndex);
        } while ( i.getVentBlock().getLocation().equals(vent.getLocation()) );
        p.teleport(vent.getLocation());
        i.setVentBlock(vent);
        return true;
    }

    public static void handleSneak(Player player) {
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        if ( a instanceof Impostor ) {
            Impostor impostor = (Impostor)a;
            if ( impostor.isVenting() ) {
                exitVent(a);
                impostor.stopVenting();
            }
        }
    }

    private static void startVentCooldown(Player player, Impostor i) {
        i.setVentCooldown(Game.ventCooldownInSeconds);
        Game.initializeVentCooldownBossBar();
        Game.ventCooldownBossBar.addPlayer(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                i.setVentCooldown(i.getVentCooldown() - 1);
                Game.ventCooldownBossBar.setTitle(ChatColor.LIGHT_PURPLE + "Vent Cooldown: " + i.getVentCooldown());
                Game.ventCooldownBossBar.setProgress((float)i.getVentCooldown() / Game.ventCooldownInSeconds);
                if (i.getVentCooldown() <= 0) {
                    Game.ventCooldownBossBar.removeAll();
                    this.cancel();
                } if ( !Game.gameRunning ) {
                    i.setVentCooldown(0);
                    Game.ventCooldownBossBar.removeAll();
                    this.cancel();
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);
    }

    private static void exitVent(AmongUsPlayer a) {
        Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
        p.setGameMode(GameMode.SURVIVAL);
        spawnVentParticleEffect(p.getLocation());
        Location loc = p.getLocation();
        loc.setY(loc.getY() + 1);
        p.teleport(loc);
    }

    private static void enterVent(Player player, Block block) {
        Location location = player.getLocation();
        spawnVentParticleEffect(location);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(block.getLocation());
    }

    private static void spawnVentParticleEffect(Location location) {
        location.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_LARGE, location, 20);
    }

}
