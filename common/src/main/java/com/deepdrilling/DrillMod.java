package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrillMod {
    public static final String MOD_ID = "deepdrilling";
    public static final String NAME = "Deep Drilling";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(DrillMod.MOD_ID);


    public static void init() {
        LOGGER.info("{} initializing! Create version: {} on platform: {}", NAME, Create.VERSION, ExpectPlatform.platformName());
        DBlocks.init(); // hold registrate in a separate class to avoid loading early on forge
        DBlockEntities.init();
        DPartialModels.init();

        // there are probably several better ways to do this whole process but this is the one I stumbled upon that actually worked
        DDrillHeads.registerBlockEntity();
    }

//    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(CreativeModeTab.TABS.length, "deepdrilling") {
//        @Override
//        public ItemStack makeIcon() {
//            return new ItemStack(DBlocks.DRILL.get());
//        }
//    };

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
