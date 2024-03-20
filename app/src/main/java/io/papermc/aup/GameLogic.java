package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

@SuppressWarnings("deprecation")
public class GameLogic {
    
    public static void run() {

        // Create and add Players to appropriate role BossBars
        BossBar crewmates = Bukkit.createBossBar(ChatColor.GREEN + "You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        BossBar impostors = Bukkit.createBossBar(ChatColor.RED + "You are an Impostor!", BarColor.RED, BarStyle.SOLID);

        // Create and add all Players to the task progress BossBar
        BossBar taskBar = Bukkit.createBossBar(ChatColor.BLUE + "Crewmate Task Progress: 0%", BarColor.BLUE, BarStyle.SEGMENTED_10);
        taskBar.setProgress(0.0);

        for (AmongUsPlayer a : Helper.amongUsPlayers) {

            Player p = Helper.getPlayerByDisplayName(a.getPlayerName());

            if (a instanceof Crewmate) {
                crewmates.addPlayer(p);
                p.sendTitle(ChatColor.GREEN + "You are Crewmate!", ChatColor.GREEN + "Complete tasks and identify the impostor!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                impostors.addPlayer(p);
                p.sendTitle(ChatColor.RED + "You are Impostor!", ChatColor.RED + "Foil the crewmates' progress!", 10, 40, 10);
            }

            taskBar.addPlayer(p);
        }

        // Game logic that executes every period, in ticks
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
            
        // 20 ticks = 1 second, under normal circumstances
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);

    }

}
