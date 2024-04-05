package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class GameLogic {
    
    public static void run() {

        // Create and add Players to appropriate role BossBars
        Game.crewmatesBossBar = Bukkit.createBossBar(ChatColor.GREEN + "You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        Game.impostorsBossBar = Bukkit.createBossBar(ChatColor.RED + "You are an Impostor!", BarColor.RED, BarStyle.SOLID);
        Game.taskBossBar = Bukkit.createBossBar(ChatColor.BLUE + "Crewmate Task Progress: 0%", BarColor.BLUE, BarStyle.SEGMENTED_10);
        Game.taskBossBar.setProgress(0.0);
        Broadcasting.sendGameStart();

        // Game logic that executes every period, in ticks
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Game.gameRunning) {
                    // Remove Players from role BossBars if game is over
                    Game.crewmatesBossBar.removeAll();
                    Game.impostorsBossBar.removeAll();
                    Game.taskBossBar.removeAll();
                    this.cancel();
                }
            }
            
        // 20 ticks = 1 second, under normal circumstances
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);

    }

}
