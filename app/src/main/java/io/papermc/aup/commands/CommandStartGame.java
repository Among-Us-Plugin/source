package io.papermc.aup.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.GameLogic;
import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandStartGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if game is already running
        // Check if there are enough players
        // Set gameRunning accordingly
        if (Game.gameRunning) {
            Component c = Component.text("ERROR: Game is already running", NamedTextColor.RED);
            sender.sendMessage(c);
            return true;
        }
        
       int onlinePlayers = Bukkit.getOnlinePlayers().size();
       int requiredPlayers = Game.numImpostors + 1;
       if (onlinePlayers < requiredPlayers) {
           String m = "ERROR: Not enough players! Currently " + onlinePlayers + " online when " + requiredPlayers + " required!";
           Component c = Component.text(m, NamedTextColor.RED);
           sender.sendMessage(c);
           return true;
       }
        
        Player[] playersArray = getOnlinePlayersArray();
        AmongUsPlayer[] amongUsPlayers = new AmongUsPlayer[playersArray.length];
        addAmongUsPlayers(playersArray, amongUsPlayers);
        castToCrewmates(amongUsPlayers);
        castImpostors(amongUsPlayers);
        
        Game.amongUsPlayers = amongUsPlayers;
        Game.initializeBossBars();
        Game.gameRunning = true;
        Broadcasting.sendGameStart("Game started by " + sender.getName());

        // Game logic
        GameLogic.run();
        
        return true;
    }

    private void castImpostors(AmongUsPlayer[] amongUsPlayers) {
        for( int i = 0; i < Game.numImpostors; i++) {
            // Generate a random index
            int r = (int)(Math.random() * (amongUsPlayers.length));
            // Generate a new index if already an Impostor
            while(amongUsPlayers[r] instanceof Impostor) {
                r = (int)(Math.random() * (amongUsPlayers.length));
            }
            amongUsPlayers[r] = new Impostor(AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayers[r]));
        }
    }

    private void addAmongUsPlayers(Player[] playersArray, AmongUsPlayer[] amongUsPlayers) {
        for (int i = 0; i < playersArray.length; i++) {
            amongUsPlayers[i] = new AmongUsPlayer(playersArray[i]);
        }
    }

    private Player[] getOnlinePlayersArray() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Player[] playersArray = players.toArray(new Player[0]);
        return playersArray;
    }

    private void castToCrewmates(AmongUsPlayer[] amongUsPlayers) {
        for (int i = 0; i < amongUsPlayers.length; i++) {
            amongUsPlayers[i] = new Crewmate(AmongUsPlayer.getPlayerByAmongUsPlayer(amongUsPlayers[i]));
        }
    }
}