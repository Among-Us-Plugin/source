package io.papermc.aup.interactions;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Impostor;

public class Vent {
    
    public static void run(Player player) {
        player.sendMessage("you vented");
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        if ( !(a instanceof Impostor) ) { return; }
        Impostor impostor = (Impostor)a;
        player.setGameMode(GameMode.SPECTATOR);
        impostor.startVenting();
    }

}
