package com.deepdrilling.blockentities;

import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IModule {
    @Nullable
    default DrillCoreBE findDrillBE(Level level, BlockPos pos) {
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

    default MutableComponent getName() {
        return MutableComponent.create(new LiteralContents("Unknown??"));
    }
    @Nullable
    DrillCoreBE getAttachedDrill();
    Direction.Axis getAxis();
    default void progressBreaking(DrillCoreBE drill) {}
    default void blockBroken(DrillCoreBE drill) {}
}
