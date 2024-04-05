package io.papermc.aup.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import io.papermc.aup.Game;
import io.papermc.aup.tasks.*;

@SuppressWarnings("deprecation")
public class InventoryClickHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!Game.gameRunning) { return; }
        event.setCancelled(true);
        String title = event.getView().getTitle();
        
        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        int clickedSlotIndex = event.getSlot();

        if (title.equals(FlipSwitches.title)) {
            if (!FlipSwitches.validIndex(clickedSlotIndex)) { return; }
            FlipSwitches.invert(clickedSlotIndex, inv);
            if (FlipSwitches.checkCompletion(inv)) {
                view.close();
                Game.increaseTaskProgress();
            }
        } else if (title.equals(Colors.title)) {
            if (!Colors.validIndex(clickedSlotIndex)) { return; }
        }
    }
}