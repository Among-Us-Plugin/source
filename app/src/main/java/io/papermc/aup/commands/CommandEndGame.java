package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandEndGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if game is running
        // Set gameRunning accordingly
        if (!Game.gameRunning) {
            Component c = Component.text("Game is not running", NamedTextColor.RED);
            sender.sendMessage(c);
            return true;
        }
        else {
            Game.endGame();
            Broadcasting.sendManualGameEnding("Game ended manually by " + sender.getName());
        }


        return true;
    }
}
