package com.deepdrilling.ponders;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Ponders {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(DrillMod.MOD_ID);

    public static PonderTag DRILLING = HELPER.createTag("drilling").item(DDrillHeads.ANDESITE.get());


    public static void register() {
        DRILLING.defaultLang("Deep Drilling", "Components related to extracting resources from deep within the earth");

        List<BlockEntry<? extends Block>> drills = new ArrayList<>(DDrillHeads.knownDrillHeads);
        drills.add(DBlocks.DRILL);
        HELPER.forComponents(drills)
                .addStoryBoard("drill/drilling", DrillingScenes::drillBasics);

        HELPER.forComponents(DBlocks.DRILL, DBlocks.BLANK_MODULE, DBlocks.COLLECTOR, DBlocks.DRILL_OVERCLOCK, DBlocks.SLUDGE_PUMP)
                .addStoryBoard("drill/modules", DrillingScenes::drillModules);

        PonderRegistry.TAGS.forTag(DRILLING)
                .add(DBlocks.DRILL)
                .add(DBlocks.COLLECTOR)
                .add(DBlocks.DRILL_OVERCLOCK)
                .add(DBlocks.SLUDGE_PUMP);
        drills.forEach(PonderRegistry.TAGS.forTag(DRILLING)::add);
        PonderRegistry.TAGS.forTag(DRILLING)
                .add(DBlocks.ASURINE_NODE)
                .add(DBlocks.CRIMSITE_NODE)
                .add(DBlocks.OCHRUM_NODE)
                .add(DBlocks.VERIDIUM_NODE);
    }
}
