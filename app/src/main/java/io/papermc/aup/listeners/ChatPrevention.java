package io.papermc.aup.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.papermc.aup.Helper;

@SuppressWarnings("deprecation")
public class ChatPrevention implements Listener {

    // Prevent Crewmates from using game chat if a meeting is not active
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (Helper.gameRunning == false) {
            return;
        }
        event.setCancelled(true); // This prevents the message from being sent
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot use chat during the game!");
    }
}
