package com.deepdrilling.blockentities;

import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class ModuleBE extends KineticBlockEntity implements IModule {
    @Nullable
    DrillCoreBE attachedDrill;
    public ModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (attachedDrill == null) {
            attachedDrill = findDrillBE(level, getBlockPos());
            if (attachedDrill != null)
                attachedDrill.findModules();
        }
    }

    @Override
    public MutableComponent getName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (attachedDrill != null)
            attachedDrill.findModules();
    }

    @Override
    public @Nullable DrillCoreBE getAttachedDrill() {
        return attachedDrill;
    }

    @Override
    public Direction.Axis getAxis() {
        return getBlockState().getValue(DirectionalKineticBlock.FACING).getAxis();
    }
}
