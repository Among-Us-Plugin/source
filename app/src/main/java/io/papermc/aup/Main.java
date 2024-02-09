package io.papermc.aup;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.aup.commands.*;
import io.papermc.aup.listeners.DamagePrevention;
import io.papermc.aup.listeners.PlayerLeaveHandling;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Custom AUP listeners
        getServer().getPluginManager().registerEvents(new DamagePrevention(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveHandling(), this);

        // Plugin commands
        // See plugin.yml file
        this.getCommand("startgame").setExecutor(new CommandStartGame());
        this.getCommand("endgame").setExecutor(new CommandEndGame());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Hello " + event.getPlayer().getName() + ", welcome to AUP indev!", NamedTextColor.AQUA));
    }

}