package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import com.deepdrilling.forge.worldgen.OreNodeManager;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DrillMod.MOD_ID)
public class DrillModForge {


    public DrillModForge() {
        DrillMod.REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                // why forge... this should not need be a thing
                .andThen(TooltipModifier.mapNull(DrillHeadTooltipsForge.create(item)))
                .andThen(TooltipModifier.mapNull(ModuleStatTooltipsForge.create(item)))
        );

        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DrillMod.REGISTRATE.registerEventListeners(eventBus);
        DrillMod.init();
        OreNodeManager.init(eventBus);
    }
}
