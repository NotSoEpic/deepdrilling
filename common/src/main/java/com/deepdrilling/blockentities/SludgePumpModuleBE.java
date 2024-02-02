package com.deepdrilling.blockentities;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SludgePumpModuleBE extends ModuleBE implements IUniqueMod, IDrillSpeedMod, IResourceWeightMod, IDrillDamageMod {
    public SludgePumpModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int speedModPriority() {
        return -100;
    }

    @Override
    public double modifySpeed(double base, double old) {
        return (base + old) / 8;
    }

    @Override
    public DrillHeadStats.WeightMultipliers getWeightMultiplier() {
        return new DrillHeadStats.WeightMultipliers(1, 0, 0);
    }

    @Override
    public ResourceLocation getIdentifier() {
        return DrillMod.id("sludge_pump");
    }

    @Override
    public int drillDamagePriority() {
        return -100;
    }

    @Override
    public double modifyDamage(double current) {
        return current * 0.5;
    }
}
