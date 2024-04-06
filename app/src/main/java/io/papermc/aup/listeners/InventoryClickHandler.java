package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.papermc.aup.Game;
import io.papermc.aup.tasks.*;

@SuppressWarnings("deprecation")
public class InventoryClickHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!Game.gameRunning) { return; }
        event.setCancelled(true);
        String title = event.getView().getTitle();

        if (title.equals(FlipSwitches.title)) {
            FlipSwitches.handleClick(event);
        } else if (title.equals(Colors.title)) {
            Colors.handleClick(event);
        }
    }
}