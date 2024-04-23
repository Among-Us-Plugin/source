package io.papermc.aup.interactions;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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

    private static void enterVent(Player player, Block block, Location location) {
        location.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_LARGE, location, 1);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(block.getLocation());
    }

}
