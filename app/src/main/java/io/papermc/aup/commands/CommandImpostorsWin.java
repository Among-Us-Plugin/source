package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.papermc.aup.Helper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandImpostorsWin implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if game is running
        if (!Helper.gameRunning) {
            Component c = Component.text("ERROR: Game is not running", NamedTextColor.RED);
            sender.sendMessage(c);
            return true;
        }

        Helper.gameRunning = false;
        Helper.impostorsWin();

        return true;
    }

}