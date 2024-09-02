package com.deepdrilling.fabric;

import com.deepdrilling.fabric.fluid.FabricSingleTank;
import com.deepdrilling.fluid.SingleTank;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class FluidTankAssociationsImpl {
    public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> associate(
            BlockEntityBuilder<T, P> builder, Function<T, SingleTank> getter) {
        return builder.onRegister(type ->
            FluidStorage.SIDED.registerForBlockEntity((block, dir) -> new FabricSingleTank(getter.apply(block)), type
        ));
    }

    public static long mbToLoaderUnits(long mb) {
        return mb * 81;
    }

    public static long loaderUnitsToMB(long droplets) {
        return droplets / 81;
    }
}
