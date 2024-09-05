package com.deepdrilling.nodes.forge;

import com.deepdrilling.forge.mixin.LootTableAccessor;
import com.deepdrilling.forge.mixin.loottable.CompositeEntryBaseMixin;
import com.deepdrilling.forge.mixin.loottable.LootItemAccessor;
import com.deepdrilling.forge.mixin.loottable.LootPoolAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.Arrays;

public class LootParserImpl {
    public static Iterable<LootPool> getPools(LootTable table) {
        return ((LootTableAccessor)table).getPools();
    }

    public static Iterable<LootPoolEntryContainer> getEntries(LootPool pool) {
        return Arrays.asList(((LootPoolAccessor)pool).getEntries());
    }

    public static Item getItem(LootItem item) {
        return ((LootItemAccessor)item).getItem();
    }

    public static Iterable<LootPoolEntryContainer> getChildren(CompositeEntryBase compositeEntry) {
        return Arrays.asList(((CompositeEntryBaseMixin)compositeEntry).getChildren());
    }
}
