package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;

public class CommandEndGame implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (!player.hasPermission("minecraft.op")) {
            Broadcasting.sendError(player, "You do not have access to this command.");
            return true;
        }

        if (!Game.gameRunning) {
            Broadcasting.sendError(player, "Game is not running");
            return true;
        }
        else {
            Game.endGame();
            Broadcasting.sendManualGameEnding("Game ended manually by " + player.getName());
        }

        return true;
    }
}
