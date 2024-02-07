package com.deepdrilling.blockentities.overclock;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class OverclockModuleRenderer extends KineticBlockEntityRenderer<OverclockModuleBE> {
    public OverclockModuleRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    // this is probably very wrong but it fixes ponder rendering
    @Override
    protected BlockState getRenderedBlockState(OverclockModuleBE be) {
        return AllBlocks.COGWHEEL.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, be.getAxis());
    }
}
