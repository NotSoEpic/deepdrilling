package com.deepdrilling.blockentities.drillcore;

import com.deepdrilling.DPartialModels;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DrillCoreInstance extends SingleRotatingInstance<DrillCoreBE> {
    public DrillCoreInstance(MaterialManager materialManager, DrillCoreBE blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);
        return getRotatingMaterial().getModel(DPartialModels.DRILL_CORE_SHAFT, referenceState, facing);
    }
}
