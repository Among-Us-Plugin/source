package io.papermc.aup.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;

public class CommandStartGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Get all online players and add them to an array
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Player[] playersArray = players.toArray(new Player[0]);
        AmongUsPlayer[] amongUsPlayers = new AmongUsPlayer[playersArray.length];
        for (int i = 0; i < playersArray.length; i++) {
            amongUsPlayers[i] = new AmongUsPlayer(playersArray[i]);
        }

        // Choose a random AU player and cast it as an Impostor
        int r = (int)(Math.random() * (amongUsPlayers.length));
        amongUsPlayers[r] = (Impostor)amongUsPlayers[r];

        // Cast the rest of the AU players as Crewmate
        for (int i = 0; i < amongUsPlayers.length; i++) {
            if(i != r) {
                amongUsPlayers[i] = (Crewmate)amongUsPlayers[i];;
            }
        }


        return true;
    }
}
