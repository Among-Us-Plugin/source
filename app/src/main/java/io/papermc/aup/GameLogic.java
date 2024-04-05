package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.tasks.FlipSwitches;

public class GameLogic {
    
    public static void run() {

        Game.initializeBossBars();
        Broadcasting.sendGameStart();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            FlipSwitches.run(p);
        }

        // Game logic that executes every period, in ticks
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Game.gameRunning) {
                    clearBossBars();
                    this.cancel();
                }
            }

            private void clearBossBars() {
                Game.crewmatesBossBar.removeAll();
                Game.impostorsBossBar.removeAll();
                Game.taskBossBar.removeAll();
            }
            
        // 20 ticks = 1 second, under normal circumstances
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 20L);

    }

}
