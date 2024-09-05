package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.blockentities.sludgepump.SludgePumpModuleBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SludgePumpModuleBlock extends ModuleBlock<SludgePumpModuleBE> {
    public SludgePumpModuleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<SludgePumpModuleBE> getBlockEntityClass() {
        return SludgePumpModuleBE.class;
    }

    @Override
    public BlockEntityType<? extends SludgePumpModuleBE> getBlockEntityType() {
        return DBlockEntities.SLUDGE_PUMP.get();
    }
}
