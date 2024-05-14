package io.papermc.aup;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.aup.classes.AmongUsPlayer;
import io.papermc.aup.classes.Body;
import io.papermc.aup.classes.Crewmate;
import io.papermc.aup.classes.Impostor;
import io.papermc.aup.classes.TaskBlock;
import io.papermc.aup.interactions.Vent;

@SuppressWarnings("deprecation")
public class Game {

    public static boolean gameRunning = false;
    public static boolean emergencyMeetingInProgress = false;
    public static int discussionPeriodDurationInSeconds = 2;
    public static int meetingDurationInSeconds = 10;
    public static int meetingCooldownInSeconds = meetingDurationInSeconds + 10;
    public static int ventCooldownInSeconds = 10;
    public static int killCooldownInSeconds = 10;

    public static AmongUsPlayer[] amongUsPlayers;
    public static ArrayList<Body> bodies = new ArrayList<Body>();
    public static ArrayList<Block> vents = new ArrayList<Block>();
    public static ArrayList<TaskBlock> taskBlocks = new ArrayList<TaskBlock>();
    
    public static Sound alertSound = Sound.BLOCK_NOTE_BLOCK_BIT;
    public static Sound errorSound = Sound.BLOCK_ANVIL_LAND;
    public static Sound taskCompletionSound = Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
    public static Sound meetingStartSound = Sound.BLOCK_NOTE_BLOCK_HARP;
    public static Sound deathSound = Sound.BLOCK_END_PORTAL_SPAWN;
    public static Sound gameEndSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
    
    public static int numImpostors = 1;

    public static BossBar crewmatesBossBar;
    public static BossBar impostorsBossBar;
    public static BossBar taskBossBar;
    public static int taskBossBarProgressAsPercentage;

    public static BossBar meetingBossBar;

    public static BossBar meetingCooldownBossBar;

    public static int numberOfTaskCompletionsForWin = 5;

    public static void restoreTaskBlocks() {
        if (taskBlocks.size() == 0) { return; }
        for (TaskBlock t : taskBlocks) {
            Location l = t.getTaskLocation();
            l.getBlock().setType(t.getTaskMaterial());
        }
    }

    public static void deleteTaskBlock(Block b) {
        TaskBlock t = new TaskBlock(b);
        taskBlocks.add(t);
        b.setType(Material.BEDROCK);
    }

    public static void initializeBossBars() {
        Game.crewmatesBossBar = Bukkit.createBossBar(ChatColor.GREEN + "You are a Crewmate!", BarColor.GREEN, BarStyle.SOLID);
        Game.impostorsBossBar = Bukkit.createBossBar(ChatColor.RED + "You are an Impostor!", BarColor.RED, BarStyle.SOLID);
        Game.taskBossBar = Bukkit.createBossBar(ChatColor.BLUE + "Crewmate Task Progress: 0%", BarColor.BLUE, BarStyle.SEGMENTED_20);
        Game.taskBossBar.setProgress(0.0);
        taskBossBarProgressAsPercentage = 0;
    }

    public static void initializeMeetingCooldownBossBar() {
        meetingCooldownBossBar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + "Meeting Cooldown: " + meetingCooldownInSeconds, BarColor.PURPLE, BarStyle.SOLID);
    }

    public static void initializeMeetingBossBar() {
        meetingBossBar = Bukkit.createBossBar(ChatColor.YELLOW + "Emergency Meeting: ", BarColor.YELLOW, BarStyle.SOLID);
    }

    public static void findVents(World world) {
        vents = findBlocksByMaterial(Vent.blockMaterial, world);
    }

    public static ArrayList<Block> findBlocksByMaterial(Material material, World world) {
        ArrayList<Block> blocks = new ArrayList<>();
        ArrayList<Chunk> loadedChunks = new ArrayList<Chunk>();
        for (Chunk chunk : world.getLoadedChunks()) {
            loadedChunks.add(chunk);
        }
        for (Chunk chunk : loadedChunks) {
            for (int x = 0; x < 16; x++) {
                for (int y = 10; y < 100; y++) {
                    for (int z = 0; z < 16; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() == material) {
                            blocks.add(block);
                        }
                    }
                }
            }
        }
        return blocks;
    }

    public static boolean checkIfBody(Block block) {
        for (Body b : bodies) {
            if (block.equals(b.getBlock())) {
                return true;
            }
        }
        return false;
    }

    public static void cleanUpBody(Block block) {
        block.setType(Material.AIR);
    }

    public static void cleanUpAllBodies() {
        for (Body b : bodies) {
            b.getBlock().setType(Material.AIR);
        }
        bodies.clear();
    }

    public static void placeBody(Player player) {
        Location location = player.getLocation();
        Block block = location.getBlock();
        if (!block.getType().equals(Material.AIR)) {
            ItemStack droppedItemStack = new ItemStack(block.getType());
            location.getWorld().dropItemNaturally(location, droppedItemStack);
        }
        block.setType(Material.PLAYER_HEAD);
        bodies.add(new Body(block));
        Skull skull = (Skull) block.getState();
        skull.setRotation(BlockFace.NORTH);
        skull.setOwningPlayer(player);
        skull.update();
        runBodyParticles(block);
    }

    public static void runBodyParticles(Block block) {
        Location location = block.getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnBodyParticleEffect(location);
                if (block.getType() != Material.PLAYER_HEAD) { this.cancel(); }
                if (!Game.gameRunning) { this.cancel(); }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main.class), 0L, 80L);
    }

    public static void killPlayer(Player player) {
        if (!gameRunning) { return; }
        player.setGameMode(GameMode.SPECTATOR);
        Location loc = player.getLocation();
        spawnBodyParticleEffect(loc);
        AmongUsPlayer a = AmongUsPlayer.getAmongUsPlayerByDisplayName(player.getDisplayName());
        a.kill();
        checkAmongUsPlayers();
    }

    private static void spawnBodyParticleEffect(Location loc) {
        World world = loc.getWorld();
        int count = 100;
        double offsetX = 0;
        double offsetY = 0.5;
        double offsetZ = 0;
        DustOptions dustOptions = new DustOptions(Color.RED, count);
        world.spawnParticle(Particle.REDSTONE, loc, count, offsetX, offsetY, offsetZ, dustOptions);
    }

    public static void endGame() {
        gameRunning = false;
        resetGameModes();
        restoreTaskBlocks();
        Broadcasting.sendSoundToAllPlayers(gameEndSound);
    }

    public static void resetGameModes() {
        for (AmongUsPlayer a : amongUsPlayers) {
            Player player = AmongUsPlayer.getPlayerByAmongUsPlayer(a);
            if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    public static void closeAllInventories() {
        for (Player p : Bukkit.getOnlinePlayers() ) {
            p.closeInventory();
        }
    }

    // Crewmates win -> end game & send appropriate titles
    public static void crewmatesWin(String reason) {
        endGame();
        Broadcasting.sendCrewmatesWin(reason);
        return;
    }

    // Impostors win -> end game & send appropriate titles
    public static void impostorsWin(String reason) {
        endGame();
        Broadcasting.sendImpostorsWin(reason);
        return;
    }

    public static void handleTaskCompletion(Player player) {

        Broadcasting.sendTaskCompletionSound(player);

        if (taskBossBar == null) { return; }
        taskBossBarProgressAsPercentage += (100 / numberOfTaskCompletionsForWin);
        double updatedProgressAsDouble = 0.01 * taskBossBarProgressAsPercentage;
        taskBossBar.setProgress(updatedProgressAsDouble);
        taskBossBar.setTitle(ChatColor.BLUE + "Crewmate Task Progress: " + taskBossBarProgressAsPercentage + "%");
    }
    
    // Check whether any AmongUsPlayers are left, end game if appropriate
    public static void checkAmongUsPlayers() {
        checkCrewmates();
        checkImpostors();
    }

    // Check whether any crewmates are left, end game if appropriate
    public static void checkCrewmates() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Crewmate && a.isALive()) {
                return;
            }
        }
        // There are no crewmates, end the game
        impostorsWin("There are no more crewmates!");
    }

    // Check whether any impostors are left, end game if appropriate
    public static void checkImpostors() {
        for (AmongUsPlayer a : Game.amongUsPlayers) {
            if (a instanceof Impostor && a.isALive()) {
                return;
            }
        }
        // There are no impostors, end the game
        crewmatesWin("There are no more impostors!");
    }
}
