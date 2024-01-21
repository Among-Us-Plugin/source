package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

public class GameLogic {
    
    public static void run() {

        // Create and add Players to appropriate role BossBars
        BossBar crewmates = Bukkit.createBossBar("You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        BossBar impostors = Bukkit.createBossBar("You are an Impostor!", BarColor.RED, BarStyle.SOLID);
        for (AmongUsPlayer a : Helper.amongUsPlayers) {
            if (a instanceof Crewmate) {
                Player p = Helper.getPlayerByDisplayName(a.getPlayerName());
                crewmates.addPlayer(p);
            }
            else if (a instanceof Impostor) {
                Player p = Helper.getPlayerByDisplayName(a.getPlayerName());
                impostors.addPlayer(p);
            }
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Helper.gameRunning) {
                    // Remove Players from role BossBars if game is over
                    crewmates.removeAll();
                    impostors.removeAll();
                    this.cancel();
                }
            }

        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);

    }

}
