package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;

public class PonderIndex {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(DrillMod.MOD_ID);

    public static void register() {
        HELPER.forComponents(DBlocks.ASURINE_NODE, DBlocks.CRIMSITE_NODE, DBlocks.OCHRUM_NODE, DBlocks.COLLECTOR)
                .addStoryBoard(DrillMod.id("node/locating"), DrillingScenes::nodeLocating);
    }
}
