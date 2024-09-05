package com.deepdrilling.nodes.fabric;

import com.deepdrilling.fabric.mixin.loottable.CompositeEntryBaseMixin;
import com.deepdrilling.fabric.mixin.loottable.LootItemAccessor;
import io.github.fabricators_of_create.porting_lib.mixin.accessors.common.accessor.ItemAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.Arrays;
import java.util.List;

public class LootParserImpl {
    public static Iterable<LootPool> getPools(LootTable table) {
        return List.of(table.pools);
    }

    public static Iterable<LootPoolEntryContainer> getEntries(LootPool pool) {
        return Arrays.asList(pool.entries);
    }

    public static Item getItem(LootItem item) {
        return ((LootItemAccessor)item).getItem();
    }

    public static Iterable<LootPoolEntryContainer> getChildren(CompositeEntryBase compositeEntry) {
        return Arrays.asList(((CompositeEntryBaseMixin)compositeEntry).getChildren());
    }
}
