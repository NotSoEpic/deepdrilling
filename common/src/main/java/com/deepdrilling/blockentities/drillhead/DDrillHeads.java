package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import com.deepdrilling.blockentities.drillhead.DrillHeadInstance;
import com.deepdrilling.blockentities.drillhead.DrillHeadRenderer;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDrillHeads {
    private static final List<BlockEntry<DrillHeadBlock>> knownDrillHeads = new ArrayList<>();
    public static final Map<ResourceLocation, PartialModel> partialModels = new HashMap<>();

    // only this stuff should need to be touched
    public static final BlockEntry<DrillHeadBlock> ANDESITE, BRASS;
    static {
        ANDESITE = createDrillHead("andesite_drill_head", 100, 1);
        BRASS = createDrillHead("brass_drill_head", 500, 0.8);
    }

    // cursed java below this line to make the stuff above the line all nice and clean looking :3
    public static BlockEntityEntry<DrillHeadBE> DRILL_HEAD_BE;
    public static BlockEntry<DrillHeadBlock> createDrillHead(String blockID, double durability, double speedModifier) {
        BlockEntry<DrillHeadBlock> block = DrillMod.REGISTRATE
                .block(blockID, DrillHeadBlock::new)
                .addLayer(() -> RenderType::cutout)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(DrillHeadStats.setDurability(durability))
                .transform(DrillHeadStats.setSpeedModifier(speedModifier))
                .item()
                .properties(p -> p.durability((int) durability))
                .build()
                .register();
        knownDrillHeads.add(block);
        // getPartialModel(new ResourceLocation(DrillMod.REGISTRATE.getModid(), blockID)); // partial model needs to be registered *early*
        getPartialModel(block.getId());
        return block;
    }

    public static void registerBlockEntity() {
        /* DRILL_HEAD_BE = DrillMod.REGISTRATE
                .blockEntity("", DrillHeadBE::new)
                // someone is going to die by my hands
                .validBlocks((BlockEntry<DrillHeadBlock>[])knownDrillHeads.toArray())
                .register(); */

        CreateBlockEntityBuilder<DrillHeadBE, CreateRegistrate> builder = DrillMod.REGISTRATE
                .blockEntity("drill_head", DrillHeadBE::new)
                .instance(() -> DrillHeadInstance::new, false);

        for (BlockEntry<DrillHeadBlock> drillHeadBlock : knownDrillHeads) {
            builder.validBlocks(drillHeadBlock);
        }

        DRILL_HEAD_BE = builder.renderer(() -> DrillHeadRenderer::new)
                .register();
    }

    public static PartialModel getPartialModel(ResourceLocation blockID) {
        if (!partialModels.containsKey(blockID)) {
            partialModels.put(blockID, new PartialModel(
                    new ResourceLocation(blockID.getNamespace(), "block/drill_heads/" + blockID.getPath())
            ));
        }
        return partialModels.get(blockID);
    }
    public static PartialModel getPartialModel(BlockState state) {
        return getPartialModel(Registry.BLOCK.getKey(state.getBlock()));
    }

    public static void init() {}
}
