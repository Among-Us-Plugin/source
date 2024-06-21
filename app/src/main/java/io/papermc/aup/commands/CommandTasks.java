package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandTasks implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (!sender.hasPermission("minecraft.op")) {
            Broadcasting.sendError(player, "You do not have access to this command.");
            return true;
        }

        if (args.length == 0) {
            Broadcasting.sendSignedMessageToPlayer(player, "Task completion requirement: " + Game.numberOfTaskCompletionsForWin, NamedTextColor.AQUA);
            return true;
        } else if (args.length == 1) {
            if (Game.gameRunning) {
                Broadcasting.sendError(player, "Cannot configure while the game is running!");
                return true;
            }
            int input = 0;
            try {
                input = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Broadcasting.sendError(player, "Please enter a number from 1 to 100.");
                return true;
            }
            if (input >= 1 && input <= 100) {
                if (Game.numberOfTaskCompletionsForWin == input) {
                    Broadcasting.sendError(player, "Task completion requirement is already set to " + Game.numberOfTaskCompletionsForWin + ".");
                    return true;
                }
                Game.numberOfTaskCompletionsForWin = input;
                Broadcasting.sendSignedMessageToPlayer(player, "Set task completion requirement to: " + Game.numberOfTaskCompletionsForWin, NamedTextColor.AQUA);
                return true;
            }
            else {
                Broadcasting.sendError(player, "Please enter a number from 1 to 100.");
                return true;
            }
        }
        return false;
    }
}

