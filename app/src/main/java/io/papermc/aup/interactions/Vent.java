package io.papermc.aup.interactions;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class Vent {
    
    public static void run(Player player, Block block) {
        player.sendMessage("you vented");
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        Location location = player.getLocation();
        enterVent(player, block, location);
        impostor.startVenting();
    }

    public static void handleSneak() {
        for ( AmongUsPlayer a : Game.amongUsPlayers ) {
            if ( a instanceof Impostor ) {
                Impostor i = (Impostor)a;
                if ( i.isVenting() ) {
                    Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
                    p.setGameMode(GameMode.SURVIVAL);
                    spawnVentEffect(p.getLocation());
                    i.stopVenting();
                }
            }
        }
    }

    private static void enterVent(Player player, Block block, Location location) {
        spawnVentEffect(location);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(block.getLocation());
    }

    private static void spawnVentEffect(Location location) {
        location.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_LARGE, location, 1);
    }

}
