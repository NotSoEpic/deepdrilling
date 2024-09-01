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

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);
        // todo beat up flywheel until it behaves (glint rendertype is invisible for instances)
//        return (blockEntity.isEnchanted() ?
//                materialManager.solid(RenderType.glint()).material(AllMaterialSpecs.ROTATING)
//                : getRotatingMaterial())
//            .getModel(DPartialModels.getDrillHeadModel(referenceState), referenceState, facing);
        return getRotatingMaterial().getModel(DPartialModels.getDrillHeadModel(referenceState), referenceState, facing);
    }
}
