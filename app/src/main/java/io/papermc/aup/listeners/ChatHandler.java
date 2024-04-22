package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class ChatHandler implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!Game.gameRunning) { return; }
        if ( Game.emergencyMeetingInProgress ) { return; }
        event.setCancelled(true);
        Broadcasting.sendError(event.getPlayer(), "You cannot use chat during the game!");
    }
}
