package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.overclock.OverclockModuleBE;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OverclockModuleBlock extends ModuleBlock<OverclockModuleBE> implements ICogWheel {
    public OverclockModuleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<OverclockModuleBE> getBlockEntityClass() {
        return OverclockModuleBE.class;
    }

    @Override
    public BlockEntityType<? extends OverclockModuleBE> getBlockEntityType() {
        return DBlockEntities.DRILL_OVERCLOCK.get();
    }
}
