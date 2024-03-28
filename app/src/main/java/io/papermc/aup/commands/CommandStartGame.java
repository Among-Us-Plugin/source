package io.papermc.aup.commands;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.GameLogic;
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
        else {
            Game.gameRunning = true;
            Component c = Component.text("Game started by " + sender.getName(), NamedTextColor.GREEN);
            Bukkit.broadcast(c);
        }

        // Get all online players and add them to an array
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Player[] playersArray = players.toArray(new Player[0]);
        AmongUsPlayer[] amongUsPlayers = new AmongUsPlayer[playersArray.length];
        for (int i = 0; i < playersArray.length; i++) {
            amongUsPlayers[i] = new AmongUsPlayer(playersArray[i]);
        }

        // Choose a random AU player and cast it as an Impostor
        int r = (int)(Math.random() * (amongUsPlayers.length));
        amongUsPlayers[r] = new Impostor(Game.getPlayerByDisplayName(amongUsPlayers[r].getPlayerName()));

        // Cast the rest of the AU players as Crewmate
        for (int i = 0; i < amongUsPlayers.length; i++) {
            if(i != r) {
                amongUsPlayers[i] = new Crewmate(Game.getPlayerByDisplayName(amongUsPlayers[i].getPlayerName()));
            }
        }

        // Store array of AU players publicly
        Game.amongUsPlayers = amongUsPlayers;

        // Game logic
        GameLogic.run();

        return true;
    }
}
