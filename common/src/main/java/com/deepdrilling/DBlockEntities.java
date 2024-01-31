package com.deepdrilling;

import com.deepdrilling.blockentities.CollectorModuleBE;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.OverclockModuleBE;
import com.deepdrilling.blockentities.drillcore.DrillCoreInstance;
import com.deepdrilling.blockentities.drillcore.DrillCoreRenderer;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class DBlockEntities {
    /**
     * for drill heads
     * @see com.deepdrilling.blockentities.drillhead.DDrillHeads
     */
    public static final BlockEntityEntry<DrillCoreBE> DRILL_CORE = DrillMod.REGISTRATE
            .blockEntity("deep_drill", DrillCoreBE::new)
            .instance(() -> DrillCoreInstance::new, false)
            .validBlocks(DBlocks.DRILL)
            .renderer(() -> DrillCoreRenderer::new)
            .register();

    public static final BlockEntityEntry<CollectorModuleBE> COLLECTOR = DrillMod.REGISTRATE
            .blockEntity("collection_filter", CollectorModuleBE::new)
            .instance(() -> ShaftInstance::new)
            .validBlocks(DBlocks.COLLECTOR)
            .renderer(() -> ShaftRenderer::new)
            .register();

    public static final BlockEntityEntry<OverclockModuleBE> DRILL_OVERCLOCK = DrillMod.REGISTRATE
            .blockEntity("drill_overclock", OverclockModuleBE::new)
            .validBlocks(DBlocks.DRILL_OVERCLOCK)
            .register();

    public static void init() {
        DrillMod.LOGGER.info("Registering block entities for " + DrillMod.NAME);
        DDrillHeads.init();
    }
}
