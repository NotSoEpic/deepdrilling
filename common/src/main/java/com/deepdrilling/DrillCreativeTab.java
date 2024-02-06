package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class DrillCreativeTab extends CreativeModeTab {
    public DrillCreativeTab(int id, String langId) {
        super(id, langId);
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> items) {
        addDrills(items);
        addNonDrills(items);
    }

    protected Collection<RegistryEntry<Item>> registeredItems() {
        return DrillMod.REGISTRATE.getAll(Registry.ITEM_REGISTRY);
    }

    public void addNonDrills(NonNullList<ItemStack> items) {
        for (RegistryEntry<Item> entry : registeredItems()) {
            if (entry.get() instanceof BlockItem blockItem) {
                if (!(blockItem.getBlock() instanceof DrillHeadBlock)) {
                    blockItem.fillItemCategory(this, items);
                }
            }
        }
    }

    public void addDrills(NonNullList<ItemStack> items) {
        for (RegistryEntry<Item> entry : registeredItems()) {
            if (entry.get() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof DrillHeadBlock) {
                    blockItem.fillItemCategory(this, items);
                }
            }
        }
    }

    @Override
    public ItemStack makeIcon() {
        return DDrillHeads.ANDESITE.get().asItem().getDefaultInstance();
    }
}
