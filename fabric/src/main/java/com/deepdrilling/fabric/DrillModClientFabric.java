package com.deepdrilling.fabric;

import com.deepdrilling.DrillModClient;
import net.fabricmc.api.ClientModInitializer;

public class DrillModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DrillModClient.init();
    }
}
