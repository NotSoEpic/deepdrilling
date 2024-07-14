package com.deepdrilling.fabric;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillCreativeTab;
import com.deepdrilling.DrillMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class DrillCreativeTabImpl {
    public static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, DrillMod.id("main"));
    private static final CreativeModeTab TAB = registerTab(FabricItemGroup.builder()
            .title(Component.literal("Deep Drilling"))
            .icon(() -> DBlocks.DRILL.asItem().getDefaultInstance())
            .displayItems(new DrillCreativeTab.ItemDisplay())
            .build());

    public static CreativeModeTab registerTab(CreativeModeTab tab) {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, KEY, tab);
        return tab;
    }

    public static void setCreativeTab() {
        DrillMod.REGISTRATE.setCreativeTab(KEY);
    }
}
