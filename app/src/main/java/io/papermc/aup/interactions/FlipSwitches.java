package io.papermc.aup.interactions;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;

@SuppressWarnings("deprecation")
public class FlipSwitches {
    
    public static String title = "FlipSwitches";
    public static int size = 9;
    
    private static Sound flipSound = Sound.BLOCK_PISTON_EXTEND;
    
    private static Material incorrectMaterial = Material.RED_STAINED_GLASS_PANE;
    private static Material correctMaterial = Material.LIME_STAINED_GLASS_PANE;

    private static ItemStack incorrectItemStack = new ItemStack(incorrectMaterial);
    private static ItemStack correctItemStack = new ItemStack(correctMaterial);

    public static void run(Player player) {
        setItemStackNames();
        Inventory inv = Bukkit.createInventory(null, size, title);
        randomizeLocations(inv);
        player.openInventory(inv);
    }
    
    public static void handleClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        Player player = (Player) event.getWhoClicked();
        int clickedSlotIndex = event.getSlot();
        if (!validIndex(clickedSlotIndex)) { return; }
        invert(clickedSlotIndex, inv, player);
        if (checkCompletion(inv)) {
            view.close();
            HumanEntity e = event.getWhoClicked();
            Game.handleTaskCompletion(e);
        }
    }

    private static void randomizeLocations(Inventory inv) {
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            int n = random.nextInt(2);
            switch(n) {
                case 0:
                    inv.setItem(i, incorrectItemStack);
                    break;
    
                case 1:
                    inv.setItem(i, correctItemStack);
                    break;
            }
        }
    }

    private static void setItemStackNames() {
        ItemMeta inCorrectItemMeta = incorrectItemStack.getItemMeta();
        inCorrectItemMeta.setDisplayName("§cOFF");
        incorrectItemStack.setItemMeta(inCorrectItemMeta);
        ItemMeta correctItemMeta = correctItemStack.getItemMeta();
        correctItemMeta.setDisplayName("§aON");
        correctItemStack.setItemMeta(correctItemMeta);
    }

    private static void invert(int slotIndex, Inventory inv, Player player) {
        ItemStack[] invContents = inv.getContents();
        Material material = invContents[slotIndex].getType();
        if (material.equals(incorrectMaterial)) {
            inv.setItem(slotIndex, correctItemStack);
        } else if (material.equals(correctMaterial)) {
            inv.setItem(slotIndex, incorrectItemStack);
        }
        Broadcasting.sendSoundToPlayer(player, flipSound);
    }

    private static boolean checkCompletion(Inventory inv) {
        ItemStack[] invContents = inv.getContents();
        for (ItemStack s : invContents) {
            Material material = s.getType();
            if(material.equals(incorrectMaterial)) {
                return false;
            }
        }
        return true;
    }

    private static boolean validIndex(int index) {
        return (index >= 0 && index < size);
    }
}
