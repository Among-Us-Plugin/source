package io.papermc.aup.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandMeetingDuration implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        Player player = (Player) sender;

        if (!player.hasPermission("minecraft.op")) {
            Broadcasting.sendError(player, "You do not have access to this command.");
            return true;
        }

        if (args.length == 0) {
            Broadcasting.sendSignedMessageToPlayer(player, "Current meeting duration: " + Game.meetingDurationInSeconds, NamedTextColor.AQUA);
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
                Broadcasting.sendError(player, "Please enter a number from 8 to 60.");
                return true;
            }
            if (input >= 8 && input <= 60) {
                if (Game.meetingDurationInSeconds == input) {
                    Broadcasting.sendError(player, "Meeting duration is already set to " + Game.meetingDurationInSeconds + ".");
                    return true;
                }
                setNewMeetingConfiguration(input);
                Broadcasting.sendSignedMessageToPlayer(player, "Set meeting duration to: " + Game.meetingDurationInSeconds, NamedTextColor.AQUA);
                return true;
            }
            else {
                Broadcasting.sendError(player, "Please enter a number from 8 to 60.");
                return true;
            }
        }
        return false;
    }

    private void setNewMeetingConfiguration(int input) {
        Game.meetingCooldownInSeconds -= Game.meetingDurationInSeconds;
        Game.meetingDurationInSeconds = input;
        Game.meetingCooldownInSeconds += Game.meetingDurationInSeconds;
        Game.discussionPeriodDurationInSeconds = Game.meetingDurationInSeconds / 2;
    }
}
