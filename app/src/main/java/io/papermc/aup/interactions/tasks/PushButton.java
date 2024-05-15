package io.papermc.aup.interactions.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class PushButton {

    public static String inventoryTitle = "PushButton";
    public static Material blockMaterial = Game.pushButtonMaterial;

    public static int buttonIndex = 4;

    public static void run(Player player, Block block) {
        Inventory inv = Bukkit.createInventory(null, 9, inventoryTitle);
        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta m = itemStack.getItemMeta();
        m.setDisplayName("§aClick me!");
        itemStack.setItemMeta(m);
        inv.setItem(buttonIndex, itemStack);
        player.openInventory(inv);
        Game.deleteTaskBlock(block);
    }

    public static void handleClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        int clickedSlotIndex = event.getSlot();
        if (clickedSlotIndex != buttonIndex) { return; }
        view.close();
        HumanEntity e = event.getWhoClicked();
        if (e instanceof Player) {
            Game.handleTaskCompletion((Player)e);
        }
    }
    
}
