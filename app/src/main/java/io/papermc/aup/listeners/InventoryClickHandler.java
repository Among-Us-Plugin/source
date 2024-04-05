package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import io.papermc.aup.Game;
import io.papermc.aup.tasks.FlipSwitches;

@SuppressWarnings("deprecation")
public class InventoryClickHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!Game.gameRunning) { return; }

        String title = event.getView().getTitle();
        if (title.equals(FlipSwitches.title)) {
            event.setCancelled(true);
            Inventory inv = event.getInventory();
            InventoryView view = event.getView();
            int clickedSlotIndex = event.getSlot();
            if (!FlipSwitches.validIndex(clickedSlotIndex)) { return; }
            FlipSwitches.invert(clickedSlotIndex, inv);
            if (FlipSwitches.checkCompletion(inv)) {
                view.close();
                Game.increaseTaskProgress();
            }
        }
    }
}