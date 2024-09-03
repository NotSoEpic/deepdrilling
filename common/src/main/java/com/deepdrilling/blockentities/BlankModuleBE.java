package com.deepdrilling.blockentities;

import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModuleBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlankModuleBE extends ModuleBE {
    public BlankModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public List<Modifier> getModifiers() {
        return List.of();
    }
}
