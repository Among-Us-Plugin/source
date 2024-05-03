package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandCooldown implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        Player player = (Player) sender;

        if (Game.gameRunning) {
            Broadcasting.sendError(player, "You cannot use this command during a game!");
            return true;
        }

        if (args.length == 0) {
            return false;
        } else if (args.length == 1) {

            String query = null;
            int response = 0;

            switch (args[0].toLowerCase()) {
                case "meeting":
                    query = "Meeting";
                    response = Game.meetingCooldownInSeconds;
                    break;
                case "vent":
                    query = "Vent";
                    response = Game.ventCooldownInSeconds;
                    break;
                case "kill":
                    query = "Kill";
                    response = Game.killCooldownInSeconds;
                    break;
            }

            if (query == null) {
                Broadcasting.sendError(player, "Invalid query.");
                return true;
            }

            String message = query + " cooldown is currently set to " + response + " seconds.";
            Broadcasting.sendSignedMessageToPlayer(player, message, NamedTextColor.AQUA);

            return true;
        } else if (args.length == 2) {
            return true;
        }
        
        
        return true;
    }
    
}
