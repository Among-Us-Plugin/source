package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.papermc.aup.tasks.FlipSwitches;

@SuppressWarnings("deprecation")
public class InventoryClickHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        String title = event.getView().getTitle();
        if (title.equals(FlipSwitches.title)) {
            event.setCancelled(true);
            int clickedSlotIndex = event.getSlot();
            FlipSwitches.invert(event.getInventory(), clickedSlotIndex);
        }

    }
}