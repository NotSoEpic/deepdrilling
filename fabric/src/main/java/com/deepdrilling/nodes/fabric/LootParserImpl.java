package com.deepdrilling.nodes.fabric;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;

public class LootParserImpl {
    public static Iterable<LootPool> getPools(LootTable table) {
        return List.of(table.pools);
    }
}
