package com.deepdrilling;

import com.jozufozu.flywheel.core.PartialModel;

public class DPartialModels {

    public static final PartialModel
    DRILL_CORE_SHAFT = block("drill_core/partial");


    public static PartialModel block(String path) {
        return new PartialModel(DrillMod.id("block/" + path));
    }

    public static void init() {
        DrillMod.LOGGER.debug("Loading partial models");
    }
}
