package com.deepdrilling.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.material.Fluid;

public class Fluids {
    public static final CFluidType WATER = new CFluidType(net.minecraft.world.level.material.Fluids.WATER, null);
    public static final CFluidType LAVE = new CFluidType(net.minecraft.world.level.material.Fluids.LAVA, null);

    @ExpectPlatform
    public static Fluid getSludge() {
        throw new AssertionError();
    }
    public static CFluidType SLUDGE = new CFluidType(getSludge(), null);
}
