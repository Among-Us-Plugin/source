package io.papermc.aup.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class Colors {
    
    public static String title = "Colors";
    public static int size = 45;
    public static Integer[] validIndices = {12, 13, 14, 21, 22, 23, 30, 31, 32};

    private static Material backgroundMaterial = Material.BLACK_STAINED_GLASS_PANE;
    private static Material redMaterial = Material.RED_STAINED_GLASS_PANE;
    private static Material orangeMaterial = Material.ORANGE_STAINED_GLASS_PANE;
    private static Material yellowMaterial = Material.YELLOW_STAINED_GLASS_PANE;
    private static Material limeMaterial = Material.LIME_STAINED_GLASS_PANE;
    private static Material greenMaterial = Material.GREEN_STAINED_GLASS_PANE;
    private static Material lightBlueMaterial = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
    private static Material blueMaterial = Material.BLUE_STAINED_GLASS_PANE;
    private static Material magentaMaterial = Material.MAGENTA_STAINED_GLASS_PANE;
    private static Material purpleMaterial = Material.PURPLE_STAINED_GLASS_PANE;


    private static ItemStack backgroundItemStack = new ItemStack(backgroundMaterial);
    private static ItemStack redItemStack = new ItemStack(redMaterial);
    private static ItemStack orangeItemStack = new ItemStack(orangeMaterial);
    private static ItemStack yellowItemStack = new ItemStack(yellowMaterial);
    private static ItemStack limeItemStack = new ItemStack(limeMaterial);
    private static ItemStack greenItemStack = new ItemStack(greenMaterial);
    private static ItemStack lightBlueItemStack = new ItemStack(lightBlueMaterial);
    private static ItemStack blueItemStack = new ItemStack(blueMaterial);
    private static ItemStack magentaItemStack = new ItemStack(magentaMaterial);
    private static ItemStack purpleItemStack = new ItemStack(purpleMaterial);

    private static ItemStack[] colorsItemStacks = {redItemStack, orangeItemStack, yellowItemStack, limeItemStack, greenItemStack, lightBlueItemStack, blueItemStack, magentaItemStack, purpleItemStack};

    public static void run(Player player) {
        Inventory inv = Bukkit.createInventory(null, size, title);
        paintBackground(inv);
        shuffleValidIndices();
        paintColors(inv);
        player.openInventory(inv);
    }

    public static boolean validIndex(int index) {
        for (int i : validIndices) {
            if (i == index) { return true; }
        }
        return false;
    }

    private static void paintColors(Inventory inv) {
        for (int i = 0; i < validIndices.length; i++) {
            inv.setItem(validIndices[i], colorsItemStacks[i]);
        }
    }

    private static void paintBackground(Inventory inv) {
        for (int i = 0; i < size; i++) {
            inv.setItem(i, backgroundItemStack);
        }
    }

    private static void shuffleValidIndices() {
        List<Integer> list = Arrays.asList(validIndices);
        Collections.shuffle(list);
        list.toArray(validIndices);
    }

}
