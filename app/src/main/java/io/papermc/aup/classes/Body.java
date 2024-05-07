package io.papermc.aup.classes;

import org.bukkit.block.Block;

public class Body {
    
    Block block;

    public Body(Block b) {
        block = b;
    }

    public Block getBlock() {
        return block;
    }

}
