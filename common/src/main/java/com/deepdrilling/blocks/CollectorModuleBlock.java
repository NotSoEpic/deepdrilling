package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.CollectorModuleBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CollectorModuleBlock extends ModuleBlock<CollectorModuleBE> {
    public CollectorModuleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<CollectorModuleBE> getBlockEntityClass() {
        return CollectorModuleBE.class;
    }

    @Override
    public BlockEntityType<? extends CollectorModuleBE> getBlockEntityType() {
        return DBlockEntities.COLLECTOR.get();
    }
}
