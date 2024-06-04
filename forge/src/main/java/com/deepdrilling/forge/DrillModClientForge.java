package com.deepdrilling.forge;

import com.deepdrilling.DrillModClient;
import net.minecraftforge.eventbus.api.IEventBus;

public class DrillModClientForge {
    public static void setupEvents(IEventBus modEventBus) {
        modEventBus.addListener(DrillEvents::clientInit);
    }

    public static void init() {
        DrillModClient.init();
    }
}
