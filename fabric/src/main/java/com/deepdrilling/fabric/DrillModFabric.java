package com.deepdrilling.fabric;

import com.deepdrilling.DrillMod;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;

public class DrillModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DrillMod.init();
        DrillMod.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), DrillMod.NAME);
        // on fabric, Registrates must be explicitly finalized and registered.
        DrillMod.REGISTRATE.register();
    }
}
