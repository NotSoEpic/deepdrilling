package com.deepdrilling;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.ponders.Ponders;

public class DrillModClient {
    public static void init() {
        DrillMod.LOGGER.info("Client initialization!");
        Ponders.register();
        DPartialModels.init();
        DDrillHeads.init();
    }
}
