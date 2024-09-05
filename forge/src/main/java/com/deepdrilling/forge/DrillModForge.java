package com.deepdrilling.forge;

import com.deepdrilling.DPartialModels;
import com.deepdrilling.DrillMod;
import com.deepdrilling.fluid.forge.FluidsImpl;
import com.deepdrilling.worldgen.forge.OreNodeStructureImpl;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DrillMod.MOD_ID)
public class DrillModForge {
    private static IEventBus BUS;

    public DrillModForge() {
        BUS = FMLJavaModLoadingContext.get().getModEventBus();
        DrillMod.REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                .andThen(TooltipModifier.mapNull(DrillHeadTooltipsForge.create(item)))
        );

//        DrillMod.BASE_CREATIVE_TAB = new DrillCreativeTab(CreativeModeTab.TABS.length, "deepdrilling.creative_tab");
        // registrate must be given the mod event bus on forge before registration
        DrillMod.REGISTRATE.registerEventListeners(BUS);
        DrillMod.init();
        FluidsImpl.init();
        OreNodeStructureImpl.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            DPartialModels.init();
            DrillModClientForge.setupEvents(BUS);
        });
    }

    public static IEventBus getBus() {
        return BUS;
    }
}
