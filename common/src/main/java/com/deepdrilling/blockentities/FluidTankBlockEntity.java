package com.deepdrilling.blockentities;

import com.deepdrilling.fluid.SingleTank;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FluidTankBlockEntity extends BlockEntity {
    public SingleTank tank = new SingleTank(1000);
    public FluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        tank.read(tag.getCompound("Tank"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("Tank", tank.write());
    }
}
