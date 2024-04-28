package io.papermc.aup;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLogic {
    
    public static void run() {

        // Game logic that executes every period, in ticks
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Game.gameRunning) {
                    cleanUp();
                }
                if ((int)Game.taskBossBar.getProgress() >= 1) {
                    Game.crewmatesWin("The crewmates finished all tasks!");
                    cleanUp();
                }
            }

            private void cleanUp() {
                Game.closeAllInventories();
                clearBossBars();
                this.cancel();
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
