package io.papermc.aup.classes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class TaskBlock {
    
    private Material taskMaterial;
    private Location taskLocation;

    public TaskBlock(Material m, Location l) {
        taskMaterial = m;
        taskLocation = l;
    }

    public TaskBlock(Block b) {
        taskMaterial = b.getType();
        taskLocation = b.getLocation();
    }

    public Material getTaskMaterial() {
        return taskMaterial;
    }

    public Location getTaskLocation() {
        return taskLocation;
    }

    public void setTaskMaterial(Material m) {
        taskMaterial = m;
    }

    public void setTaskLocation(Location l) {
        taskLocation = l;
    }

}
