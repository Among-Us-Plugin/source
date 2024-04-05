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
    
    private static ItemStack red = new ItemStack(Material.REDSTONE_TORCH);
    private static ItemStack blue = new ItemStack(Material.SOUL_TORCH);

    public static void run(Player player) {
        Inventory inv = Bukkit.createInventory(null, size, title);
        
        
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            int n = random.nextInt(2);
            switch(n) {
                case 0:
                    inv.setItem(i, red);
                    break;
    
                case 1:
                    inv.setItem(i, blue);
                    break;
            }
        }
        player.openInventory(inv);
    }

    public static void invert(Inventory inv, int slotIndex) {
        ItemStack[] invContents = inv.getContents();
        Material material = invContents[slotIndex].getType();
        if (material.equals(Material.REDSTONE_TORCH)) {
            inv.setItem(slotIndex, blue);
        } else if (material.equals(Material.SOUL_TORCH)) {
            inv.setItem(slotIndex, red);
        }
    }
}
