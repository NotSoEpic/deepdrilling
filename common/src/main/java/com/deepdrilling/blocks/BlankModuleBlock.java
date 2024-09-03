package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.BlankModuleBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlankModuleBlock extends ModuleBlock<BlankModuleBE> {
    public BlankModuleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BlankModuleBE> getBlockEntityClass() {
        return BlankModuleBE.class;
    }

    @Override
    public BlockEntityType<? extends BlankModuleBE> getBlockEntityType() {
        return DBlockEntities.BLANK_MODULE.get();
    }
}
