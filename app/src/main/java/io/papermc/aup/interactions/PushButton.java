package io.papermc.aup.interactions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class PushButton {

    public static String inventoryTitle = "PushButton";
    public static Material blockMaterial = Material.TARGET;

    public static int buttonIndex = 4;

    public static void run(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, inventoryTitle);
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        inv.setItem(buttonIndex, itemStack);
        player.openInventory(inv);
    }

    public static void handleClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        int clickedSlotIndex = event.getSlot();
        if (clickedSlotIndex != buttonIndex) { return; }
        view.close();
        HumanEntity e = event.getWhoClicked();
        Game.handleTaskCompletion(e);
    }
    
}
