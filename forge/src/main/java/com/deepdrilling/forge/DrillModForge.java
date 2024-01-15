package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DrillMod.MOD_ID)
public class DrillModForge {
    public DrillModForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DrillMod.REGISTRATE.registerEventListeners(eventBus);
        DrillMod.init();
    }
}
