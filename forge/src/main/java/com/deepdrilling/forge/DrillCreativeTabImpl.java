package com.deepdrilling.forge;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillCreativeTab;
import com.deepdrilling.DrillMod;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DrillCreativeTabImpl {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DrillMod.MOD_ID);

    private static RegistryObject<CreativeModeTab> TAB = REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Components.translatable("itemGroup.deepdrilling.main"))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                    .icon(DBlocks.DRILL::asStack)
                    .displayItems(new DrillCreativeTab.ItemDisplay())
                    .build());

    public static void setCreativeTab() {
        DrillMod.REGISTRATE.setCreativeTab(TAB);
    }
}
