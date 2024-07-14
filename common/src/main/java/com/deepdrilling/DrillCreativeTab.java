package com.deepdrilling;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

public class DrillCreativeTab {
    @ExpectPlatform
    public static void setCreativeTab() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void register() {
        throw new AssertionError();
    }

    public static class ItemDisplay implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
            output.acceptAll(DrillMod.REGISTRATE.getAll(Registries.ITEM).stream()
                    .filter(item -> !(item.get() instanceof SequencedAssemblyItem))
                    .map(item -> item.get().getDefaultInstance()).toList());
        }
    }
}
