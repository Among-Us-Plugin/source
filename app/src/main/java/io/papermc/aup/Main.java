package io.papermc.aup;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.aup.commands.*;
import io.papermc.aup.listeners.*;
import io.papermc.aup.tasks.FlipSwitches;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Custom AUP listeners
        getServer().getPluginManager().registerEvents(new ChatPrevention(), this);
        getServer().getPluginManager().registerEvents(new DamagePrevention(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathHandler(), this);
        getServer().getPluginManager().registerEvents(new ImpostorKill(), this);
        getServer().getPluginManager().registerEvents(new DeathMessagePrevention(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickHandler(), this);

        // Plugin commands
        // See plugin.yml file
        this.getCommand("startgame").setExecutor(new CommandStartGame());
        this.getCommand("endgame").setExecutor(new CommandEndGame());
        this.getCommand("crewmateswin").setExecutor(new CommandCrewmatesWin());
        this.getCommand("impostorswin").setExecutor(new CommandImpostorsWin());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Hello " + event.getPlayer().getName() + ", welcome to AUP indev!", NamedTextColor.AQUA));
        FlipSwitches.run(event.getPlayer());
    }

}