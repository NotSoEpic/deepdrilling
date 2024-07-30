package com.deepdrilling.jei;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DPartialModels;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedDrill extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        draw(guiGraphics, xOffset, yOffset, null);
    }

    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, BlockState state) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(DBlocks.DRILL.getDefaultState().setValue(DrillBlock.FACING, Direction.DOWN))
                .atLocal(0, -1, 0)
                .scale(scale)
                .render(guiGraphics);


        matrixStack.pushPose();
        matrixStack.rotateAround(Axis.XN.rotationDegrees(90), 0.5f, 0.5f, 0.5f);
        blockElement(DPartialModels.DRILL_CORE_SHAFT)
                // oh god matrix transformations
                .atLocal(0, 0, -2)
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(guiGraphics);

        blockElement(DPartialModels.getDrillHeadModel(DDrillHeads.ANDESITE.getId()))
                .atLocal(0, 0, -1)
                .rotateBlock(0, 0, getCurrentAngle())
                .scale(scale)
                .render(guiGraphics);
        matrixStack.popPose();

        if (state != null) {
            GuiGameElement.of(state)
                    .atLocal(0, 1, 0)
                    .scale(scale)
                    .render(guiGraphics);
        }
        matrixStack.popPose();
    }
}
