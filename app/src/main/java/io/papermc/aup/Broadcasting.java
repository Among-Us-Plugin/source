package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class Broadcasting {

    public static void sendSoundToPlayer(Player player, Sound sound, Float pitch) {
        player.playSound(player.getLocation(), sound, 1.0F, pitch);
    }
    
    public static void sendSoundToPlayer(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
    }

    public static void sendTaskCompletionSound(HumanEntity entity) {
        if ( entity instanceof Player) {
            Player p = (Player) entity;
            sendSoundToPlayer(p, Game.taskCompletionSound);
        }
    }

    public static void sendError(Player player, String message) {
        sendSignedMessageToPlayer(player, message, NamedTextColor.RED);
        sendSoundToPlayer(player, Game.errorSound);
    }

    public static void sendDeathSound(Player player) {
        Location location = player.getLocation();
        Sound sound = Game.deathSound;
        player.playSound(location, sound, 1.0F, 1.0F);
    }

    public static void sendSoundToAllPlayers(Sound sound) {
        for ( Player p : Bukkit.getOnlinePlayers() ) {
            Location location = p.getLocation();
            p.playSound(location, sound, 1.0F, 1.0F);
        }
    }
    
    public static void sendCrewmatesWin(String reason) {
        broadcastSignedMessage("Game Ended: " + reason, NamedTextColor.GOLD);
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
        broadcastSignedMessage("Game Ended: " + reason, NamedTextColor.GOLD);
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

    public static void sendGameStart(String m) {
        broadcastSignedMessage(m, NamedTextColor.GREEN);
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
        broadcastSignedMessage(m, NamedTextColor.GOLD);
        
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            Player p = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            p.sendTitle(ChatColor.GOLD + "Game Ended", ChatColor.GOLD + m, 10, 40, 10);
        }
    }

    public static void sendSignedMessageToPlayer(Player player, String message, NamedTextColor color) {
        String output = "[AUP] " + message;
        Component c = Component.text(output, color);
        player.sendMessage(c);
    }

    public static void broadcastSignedMessage(String message, NamedTextColor color) {
        String output = "[AUP] " + message;
        Component c = Component.text(output, color);
        Bukkit.broadcast(c);
    }
}
