package com.deepdrilling.nodes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public class OreNode {
    public final Block source;
    public final LootTable loot;

    OreNode(Block source, LootTable loot) {
        this.source = source;
        this.loot = loot;
    }

    public static void register(Block source, LootTable loot) {

    }
}
