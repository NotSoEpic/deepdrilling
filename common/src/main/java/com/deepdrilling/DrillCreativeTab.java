package com.deepdrilling;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

public class DrillCreativeTab {
    // todo: why doesnt this work? :(
    @ExpectPlatform
    public static void setCreativeTab() {
        throw new AssertionError();
    }

    public static void register() {}

    public static class ItemDisplay implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            output.acceptAll(DrillMod.REGISTRATE.getAll(Registries.ITEM).stream()
                    .map(item -> item.get().getDefaultInstance()).toList());
        }
    }
}
