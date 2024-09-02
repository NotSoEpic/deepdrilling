package com.deepdrilling.blockentities.module;

import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.module.Module;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class ModuleBE extends KineticBlockEntity implements Module {
    @Nullable
    DrillCoreBE attachedDrill;
    public ModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public DrillCoreBE findDrillBE(Level level, BlockPos pos) {
        if (getAttachedDrill() != null)
            return getAttachedDrill();
        for (int i = 1; i < DrillCoreBE.searchDist + 1; i++) {
            if (level.getBlockEntity(pos.relative(getAxis(), i)) instanceof DrillCoreBE drillBlockEntity)
                return drillBlockEntity;
            if (level.getBlockEntity(pos.relative(getAxis(), -i)) instanceof DrillCoreBE drillBlockEntity)
                return drillBlockEntity;
        }
        return null;
    }

    /**
     * Evaluated from nearest module to furthest: if any name already exists, this module does not apply any effects
     */
    public Set<ResourceLocation> getMutuallyExclusiveNames() {
        return Set.of();
    }

    public @Nullable DrillCoreBE getAttachedDrill() {
        return attachedDrill;
    }

    public Direction.Axis getAxis() {
        return getBlockState().getValue(DirectionalKineticBlock.FACING).getAxis();
    }

    @Override
    public MutableComponent getName() {
        return getBlockState().getBlock().getName();
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
    public void invalidate() {
        super.invalidate();
        if (attachedDrill != null)
            attachedDrill.findModules();
    }
}
