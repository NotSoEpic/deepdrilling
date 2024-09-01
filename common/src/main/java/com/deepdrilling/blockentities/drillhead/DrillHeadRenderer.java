package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DPartialModels;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.state.BlockState;

public class DrillHeadRenderer extends KineticBlockEntityRenderer<DrillHeadBE> {
    public DrillHeadRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrillHeadBE be, BlockState state) {
        return CachedBufferer.partialFacing(DPartialModels.getDrillHeadModel(state), state);
    }

    @Override
    protected void renderSafe(DrillHeadBE be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel())) return;

        BlockState state = getRenderedBlockState(be);
        RenderType type = getRenderType(be, state);
        if (type != null) {
            VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, type, true, be.isEnchanted());
            renderRotatingBuffer(be, getRotatedModel(be, state), ms, consumer, light);
        }
    }
}
