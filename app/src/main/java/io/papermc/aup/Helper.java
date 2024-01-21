package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Helper {
    
    public static Player getPlayerByDisplayName(String displayName) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getDisplayName().equals(displayName)) {
                return p;
            }
        }
        return null;
    }

}