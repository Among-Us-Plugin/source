package io.papermc.aup;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.aup.commands.*;
import io.papermc.aup.commands.completers.*;
import io.papermc.aup.listeners.*;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Custom AUP listeners
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new AttackHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(), this);
        getServer().getPluginManager().registerEvents(new ImpostorKillHandler(), this);
        getServer().getPluginManager().registerEvents(new DeathMessageHandler(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(), this);
        getServer().getPluginManager().registerEvents(new InteractionHandler(), this);
        getServer().getPluginManager().registerEvents(new MovementHandler(), this);
        getServer().getPluginManager().registerEvents(new SneakHandler(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakHandler(), this);

        // Plugin commands
        // See plugin.yml file
        this.getCommand("startgame").setExecutor(new CommandStartGame());
        this.getCommand("endgame").setExecutor(new CommandEndGame());
        this.getCommand("impostors").setExecutor(new CommandImpostors());
        this.getCommand("cooldown").setExecutor(new CommandCooldown());
        this.getCommand("tasks").setExecutor(new CommandTasks());
        this.getCommand("meetingduration").setExecutor(new CommandMeetingDuration());

        // Plugin completers
        this.getCommand("cooldown").setTabCompleter(new CooldownCompleter());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game.findVents(event.getPlayer().getWorld());
    }

}