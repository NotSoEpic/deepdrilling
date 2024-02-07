package com.deepdrilling;

import com.deepdrilling.ponders.PonderIndex;

public class DrillModClient {
    public static void init() {
        DrillMod.LOGGER.info("Client initialization!");
        PonderIndex.register();
    }
}
