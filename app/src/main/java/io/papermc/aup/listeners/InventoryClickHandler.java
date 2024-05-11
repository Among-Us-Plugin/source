package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.papermc.aup.Game;
import io.papermc.aup.interactions.*;
import io.papermc.aup.interactions.tasks.Colors;
import io.papermc.aup.interactions.tasks.FlipSwitches;
import io.papermc.aup.interactions.tasks.PushButton;

@SuppressWarnings("deprecation")
public class InventoryClickHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!Game.gameRunning) { return; }
        event.setCancelled(true);
        String title = event.getView().getTitle();

        if (title.equals(FlipSwitches.inventoryTitle)) {
            FlipSwitches.handleClick(event);
        } else if (title.equals(Colors.inventoryTitle)) {
            Colors.handleClick(event);
        } else if (title.equals(PushButton.inventoryTitle)) {
            PushButton.handleClick(event);
        } else if(title.equals(EmergencyMeeting.inventoryTitle)){
            EmergencyMeeting.handleClick(event);
        }
    }
}