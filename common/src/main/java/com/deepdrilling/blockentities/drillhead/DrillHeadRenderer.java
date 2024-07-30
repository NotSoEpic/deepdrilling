package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class DrillHeadRenderer extends KineticBlockEntityRenderer<DrillHeadBE> {
    public DrillHeadRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrillHeadBE be, BlockState state) {
        return CachedBufferer.partialFacing(DPartialModels.getDrillHeadModel(state), state);
    }
}
