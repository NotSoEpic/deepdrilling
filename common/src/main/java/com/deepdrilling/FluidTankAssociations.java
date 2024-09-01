package com.deepdrilling;

import com.deepdrilling.fluid.SingleTank;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

public class FluidTankAssociations {
    @ExpectPlatform
    public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> associate(
            BlockEntityBuilder<T, P> builder, Function<T, SingleTank> getter) {
        throw new AssertionError();
    }

    /**
     * Converts mb to droplets on fabric
     * @param mb millibuckets
     * @return * 1 -> mb (forge), * 81 -> droplets (fabric)
     */
    @ExpectPlatform
    public static long mbToLoaderUnits(long mb) {
        throw new AssertionError();
    }
}
