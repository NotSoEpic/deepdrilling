package com.deepdrilling.blocks;

import com.deepdrilling.DBlockEntities;
import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.SludgePumpModuleBE;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SludgePumpModuleBlock extends ModuleBlock<SludgePumpModuleBE> implements IModuleTooltip {
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

    @Override
    public boolean unique() {
        return true;
    }

    @Override
    public int damageModifier() {
        return -1;
    }

    @Override
    public int speedModifier() {
        return -1;
    }

    @Override
    public DrillHeadStats.WeightMultipliers getWeightMultipliers() {
        return new DrillHeadStats.WeightMultipliers(2, 0, 0);
    }
}
