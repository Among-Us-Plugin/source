package io.papermc.aup.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.papermc.aup.Helper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandEndGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if game is running
        // Set gameRunning accordingly
        if (Helper.gameRunning) {
            Helper.gameRunning = false;
            Component c = Component.text("Game ended by " + sender.getName(), NamedTextColor.GOLD);
            Bukkit.broadcast(c);
        }
        else {
            Component c = Component.text("ERROR: Game is not running", NamedTextColor.RED);
            sender.sendMessage(c);
        }


        return true;
    }
}
