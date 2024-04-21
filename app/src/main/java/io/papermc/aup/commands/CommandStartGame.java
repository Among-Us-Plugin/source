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

public class CommandStartGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (Game.gameRunning) {
            Broadcasting.sendError((Player) sender, "Game is already running");
            return true;
        }
        
       int onlinePlayers = Bukkit.getOnlinePlayers().size();
       int requiredPlayers = Game.numImpostors + 1;
       if (onlinePlayers < requiredPlayers) {
           String m = "Not enough players! Currently " + onlinePlayers + " online when " + requiredPlayers + " required!";
           Broadcasting.sendError((Player) sender, m);
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

        Broadcasting.sendAlertSound();
        
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