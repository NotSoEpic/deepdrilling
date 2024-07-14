package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.worldgen.OreNodes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrillMod {
    public static final String MOD_ID = "deepdrilling";
    public static final String NAME = "Deep Drilling";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(DrillMod.MOD_ID);
//    public static CreativeModeTab BASE_CREATIVE_TAB;


    public static void init() {
        LOGGER.info("{} initializing! Create version: {} on platform: {}", NAME, Create.VERSION, ExpectPlatform.platformName());

        DrillCreativeTab.setCreativeTab();
        DBlocks.init(); // hold registrate in a separate class to avoid loading early on forge
        DItems.init();
        DBlockEntities.init();
        DPartialModels.init();

        // there are probably several better ways to do this whole process but this is the one I stumbled upon that actually worked
        DDrillHeads.registerBlockEntity();
        OreNodes.init();
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
