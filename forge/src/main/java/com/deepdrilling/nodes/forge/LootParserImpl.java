package com.deepdrilling.nodes.forge;

import com.deepdrilling.forge.mixin.LootTableAccessor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootParserImpl {
    public static Iterable<LootPool> getPools(LootTable table) {
        return ((LootTableAccessor)table).getPools();
    }
}
