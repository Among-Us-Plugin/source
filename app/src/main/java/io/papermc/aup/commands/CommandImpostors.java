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

        if (args.length == 0) {
            Broadcasting.sendSignedMessageToPlayer(player, "Number of impostors per game: " + Game.numImpostors, NamedTextColor.AQUA);
            return true;
        } else if (args.length == 1) {
            if (Integer.parseInt(args[0]) >= 1 && Integer.parseInt(args[0]) <= 5) {
                Game.numImpostors = Integer.parseInt(args[0]);
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
