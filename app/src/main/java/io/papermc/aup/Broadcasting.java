package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class Broadcasting {
    
    public static void sendCrewmatesWin(String reason) {
        Component c = Component.text("Game Ended: " + reason, NamedTextColor.GOLD);
        Bukkit.broadcast(c);
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + "The crewmates won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.RED + "DEFEAT", ChatColor.RED + "The crewmates won!", 10, 40, 10);
            }
        }
    }

    public static void sendImpostorsWin(String reason) {
        Component c = Component.text("Game Ended: " + reason, NamedTextColor.GOLD);
        Bukkit.broadcast(c);
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (a instanceof Crewmate) {
                p.sendTitle(ChatColor.RED + "DEFEAT", ChatColor.RED + "The impostors won!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                p.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + "The impostors won!", 10, 40, 10);
            }
        }
    }

    public static void sendGameStart() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (a instanceof Crewmate) {
                Game.crewmatesBossBar.addPlayer(p);
                p.sendTitle(ChatColor.GREEN + "You are Crewmate!", ChatColor.GREEN + "Complete tasks and identify the impostor!", 10, 40, 10);
            }
            else if (a instanceof Impostor) {
                Game.impostorsBossBar.addPlayer(p);
                p.sendTitle(ChatColor.RED + "You are Impostor!", ChatColor.RED + "Foil the crewmates' progress!", 10, 40, 10);
            }
            Game.taskBossBar.addPlayer(p);
        }
    }

    public static void sendManualGameEnding(String m) {
        Component c = Component.text(m, NamedTextColor.GOLD);
        Bukkit.broadcast(c);
        
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            p.sendTitle(ChatColor.GOLD + "Game Ended", ChatColor.GOLD + m, 10, 40, 10);
        }
    }
}
