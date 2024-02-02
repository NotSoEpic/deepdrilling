package com.deepdrilling.blockentities.overclock;

import com.deepdrilling.blockentities.IDrillDamageMod;
import com.deepdrilling.blockentities.IDrillSpeedMod;
import com.deepdrilling.blockentities.ModuleBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OverclockModuleBE extends ModuleBE implements IDrillSpeedMod, IDrillDamageMod {
    public OverclockModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public double modifySpeed(double baseSpeed, double old) {
        return old * 0.75f - baseSpeed * 0.05f;
    }

    @Override
    public double modifyDamage(double current) {
        return current + 1;
    }

    @Override
    public int drillDamagePriority() {
        return 1;
    }
}
