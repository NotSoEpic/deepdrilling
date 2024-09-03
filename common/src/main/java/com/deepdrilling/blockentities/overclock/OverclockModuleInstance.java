package com.deepdrilling.blockentities.overclock;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class OverclockModuleInstance extends SingleRotatingInstance<OverclockModuleBE> {
    public OverclockModuleInstance(MaterialManager materialManager, OverclockModuleBE blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        Direction.Axis axis = blockEntity.getBlockState().getValue(BlockStateProperties.AXIS);
        return getRotatingMaterial().getModel(AllBlocks.COGWHEEL.getDefaultState().setValue(CogWheelBlock.AXIS, axis));
    }
}
