package io.papermc.aup.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.joml.Random;

@SuppressWarnings("deprecation")
public class FlipSwitches {
    
    public static String title = "Torches!";
    public static int size = 9;
    
    private static Material incorrectMaterial = Material.REDSTONE_TORCH;
    private static Material correctMaterial = Material.SOUL_TORCH;

    private static ItemStack incorrectItemStack = new ItemStack(incorrectMaterial);
    private static ItemStack correctItemStack = new ItemStack(correctMaterial);

    public static void run(Player player) {
        Inventory inv = Bukkit.createInventory(null, size, title);
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
        player.openInventory(inv);
    }

    public static void invert(int slotIndex, Inventory inv) {
        ItemStack[] invContents = inv.getContents();
        Material material = invContents[slotIndex].getType();
        if (material.equals(incorrectMaterial)) {
            inv.setItem(slotIndex, correctItemStack);
        } else if (material.equals(correctMaterial)) {
            inv.setItem(slotIndex, incorrectItemStack);
        }
    }

    public static boolean checkCompletion(Inventory inv) {
        ItemStack[] invContents = inv.getContents();
        for (ItemStack s : invContents) {
            Material material = s.getType();
            if(material.equals(incorrectMaterial)) {
                return false;
            }
        }
        return true;
    }
}
