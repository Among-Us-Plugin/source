package io.papermc.aup.interactions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class Colors {
    
    public static String inventoryTitle = "Colors";
    public static Material blockMaterial = Material.SCULK;

    private static Sound progressSound = Sound.BLOCK_NOTE_BLOCK_CHIME;
    private static Sound mistakeSound = Sound.ENTITY_GHAST_SCREAM;

    private static int inventorySize = 54;
    private static int progressBarStartIndex = inventorySize - 9;

    // MUST BE CONFIGURED WHEN RESIZING
    private static Integer[] validIndices = {12, 13, 14, 21, 22, 23, 30, 31, 32};

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
        Inventory inv = Bukkit.createInventory(null, inventorySize, inventoryTitle);
        renameItemStacks();
        paintBackground(inv);
        shuffleValidIndices();
        paintColors(inv);
        player.openInventory(inv);
    }

    public static void handleClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        InventoryView view = event.getView();
        Player player = (Player) event.getWhoClicked();
        int clickedSlotIndex = event.getSlot();
        if (!validIndex(clickedSlotIndex)) { return; }
        int progress = getProgress(inv);
        if (!isCorrect(clickedSlotIndex, progress, inv)) {
            Broadcasting.sendSoundToPlayer(player, mistakeSound);
            resetProgress(inv);
            return;
        }
        incrementProgress(inv);
        if (taskIsFinished(inv)) {
            view.close();
            HumanEntity e = event.getWhoClicked();
            Game.handleTaskCompletion(e);
            return;
        }
        Float pitch = (float) ((getProgress(inv) + 8) / 16.0);
        Broadcasting.sendSoundToPlayer(player, progressSound, pitch);
    }

    private static boolean taskIsFinished(Inventory inv) {
        return getProgress(inv) == 9;
    }

    private static boolean isCorrect(int clickedSlotIndex, int progress, Inventory inv) {
        if (!inv.getItem(clickedSlotIndex).equals(colorsItemStacks[progress])) { return false; }
        return true;
    }

    private static void resetProgress(Inventory inv) {
        for (int i = progressBarStartIndex; i < inventorySize; i++) {
            inv.setItem(i, backgroundItemStack);
        }
    }

    private static void incrementProgress(Inventory inv) {
        int progress = getProgress(inv);
        int nextOpenIndex = progress + progressBarStartIndex;
        inv.setItem(nextOpenIndex, colorsItemStacks[progress]);
    }

    private static int getProgress(Inventory inv) {
        int progress = 0;
        for (int i = progressBarStartIndex; i < inventorySize; i++) {
            if (!inv.getItem(i).equals(backgroundItemStack)) {
                progress++;
            }
        }
        return progress;
    }

    private static boolean validIndex(int index) {
        for (int i : validIndices) {
            if (i == index) { return true; }
        }
        return false;
    }

    private static void renameItemStacks() {
        ItemMeta b = backgroundItemStack.getItemMeta();
        b.setDisplayName(" ");
        backgroundItemStack.setItemMeta(b);
        for (ItemStack s : colorsItemStacks) {
            ItemMeta m = s.getItemMeta();
            m.setDisplayName("§cC§6o§el§ao§br§9s§d!");
            s.setItemMeta(m);
        }
    }

    private static void paintColors(Inventory inv) {
        for (int i = 0; i < validIndices.length; i++) {
            inv.setItem(validIndices[i], colorsItemStacks[i]);
        }
    }

    private static void paintBackground(Inventory inv) {
        for (int i = 0; i < inventorySize; i++) {
            inv.setItem(i, backgroundItemStack);
        }
    }

    private static void shuffleValidIndices() {
        List<Integer> list = Arrays.asList(validIndices);
        Collections.shuffle(list);
        list.toArray(validIndices);
    }

}
