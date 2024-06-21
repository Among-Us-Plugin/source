package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandImpostors implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (!sender.hasPermission("minecraft.op")) {
            Broadcasting.sendError(player, "You do not have access to this command.");
            return true;
        }

        if (args.length == 0) {
            Broadcasting.sendSignedMessageToPlayer(player, "Number of impostors per game: " + Game.numImpostors, NamedTextColor.AQUA);
            return true;
        } else if (args.length == 1) {
            if (Game.gameRunning) {
                Broadcasting.sendError(player, "Cannot configure number of impostors during a game!");
                return true;
            }
            int input = 0;
            try {
                input = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Broadcasting.sendError(player, "Please enter a number from 1 to 5.");
                return true;
            }
            if (input >= 1 && input <= 5) {
                if (Game.numImpostors == input) {
                    Broadcasting.sendError(player, "Number of impostors is already set to " + Game.numImpostors + ".");
                    return true;
                }
                Game.numImpostors = input;
                Broadcasting.sendSignedMessageToPlayer(player, "Set number of impostors to: " + Game.numImpostors, NamedTextColor.AQUA);
                return true;
            }
            else {
                Broadcasting.sendError(player, "Please enter a number from 1 to 5.");
                return true;
            }
        }

        return false;
    }
}
