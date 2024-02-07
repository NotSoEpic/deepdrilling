package com.deepdrilling;

import com.deepdrilling.ponders.Ponders;

public class DrillModClient {
    public static void init() {
        DrillMod.LOGGER.info("Client initialization!");
        Ponders.register();
    }
}
