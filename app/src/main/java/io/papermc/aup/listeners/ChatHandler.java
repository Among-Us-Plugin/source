package io.papermc.aup.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class ChatHandler implements Listener {

    // Prevent Crewmates from using game chat if a meeting is not active
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!Game.gameRunning) { return; }
        event.setCancelled(true); // This prevents the message from being sent
        event.getPlayer().sendMessage(ChatColor.RED + "You cannot use chat during the game!");
    }
}
