package com.deepdrilling.blockentities.drillhead;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DrillHeadRenderer extends KineticBlockEntityRenderer<DrillHeadBE> {
    public DrillHeadRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected BlockState getRenderedBlockState(DrillHeadBE be) {
        return shaft(getRotationAxisOf(be));
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrillHeadBE be, BlockState state) {
        return CachedBufferer.partialFacing(DDrillHeads.getPartialModel(state), state);
    }
}
