package com.deepdrilling.forge;

import com.deepdrilling.fluid.SingleTank;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.function.Function;

public class FluidTankAssociationsImpl {
    public static HashMap<BlockEntityType<BlockEntity>, Function<BlockEntity, SingleTank>> getters = new HashMap<>();
    public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> associate(
            BlockEntityBuilder<T, P> builder, Function<T, SingleTank> getter) {
        return builder.onRegister((type) -> getters.put((BlockEntityType<BlockEntity>) type, (Function<BlockEntity, SingleTank>) getter));
    }

    public static long mbToLoaderUnits(long mb) {
        return mb;
    }
}
