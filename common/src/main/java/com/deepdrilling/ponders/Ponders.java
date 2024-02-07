package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Ponders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(DrillMod.MOD_ID);

    public static void register() {
        HELPER.forComponents(DBlocks.ASURINE_NODE, DBlocks.CRIMSITE_NODE, DBlocks.OCHRUM_NODE, DBlocks.VERIDIUM_NODE)
                .addStoryBoard("node/locating", DrillingScenes::nodeLocating)
                .addStoryBoard("node/biomes", DrillingScenes::nodeBiomes);

        List<BlockEntry<? extends Block>> drills = new ArrayList<>(DDrillHeads.knownDrillHeads);
        drills.add(DBlocks.DRILL);
        HELPER.forComponents(drills)
                .addStoryBoard("drill/drilling", DrillingScenes::drillBasics);

        HELPER.forComponents(DBlocks.DRILL, DBlocks.COLLECTOR, DBlocks.DRILL_OVERCLOCK, DBlocks.SLUDGE_PUMP)
                .addStoryBoard("drill/modules", DrillingScenes::drillModules);
    }
}
