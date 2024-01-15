package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DPartialModels;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DrillHeadInstance extends SingleRotatingInstance<DrillHeadBE> {
    public DrillHeadInstance(MaterialManager materialManager, DrillHeadBE blockEntity) {
        super(materialManager, blockEntity);
    }

//  todo: the culprit
    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);
        return getRotatingMaterial().getModel(DDrillHeads.getPartialModel(referenceState), referenceState, facing);
    }
}
