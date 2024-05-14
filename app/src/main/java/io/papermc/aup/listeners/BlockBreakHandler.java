package io.papermc.aup.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.papermc.aup.Broadcasting;
import io.papermc.aup.Game;

public class BlockBreakHandler implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!Game.gameRunning) { return; }
        Material m = event.getBlock().getType();
        for (Material protectedMaterial : Game.protectedMaterials) {
            if (protectedMaterial.equals(m)) {
                event.setCancelled(true);
                Broadcasting.sendError(event.getPlayer(),"You cannot break that block during the game!");
            }
        }
    }
}
