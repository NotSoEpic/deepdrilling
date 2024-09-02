package com.deepdrilling.fluid.forge;

import com.deepdrilling.DrillMod;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidsImpl {
    public static final FluidEntry<ForgeFlowingFluid.Flowing> SLUDGE = DrillMod.REGISTRATE
            .fluid("sludge", DrillMod.id("block/sludge_still"), DrillMod.id("block/sludge_flow"), ForgeFlowingFluid.Flowing::new)
//            .standardFluid("sludge")
            .lang("Sludge")
            .tag(FluidTags.WATER)
            .properties(b -> b.viscosity(2000)
                    .density(2000))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(30)
                    .explosionResistance(100))
            .source(ForgeFlowingFluid.Source::new)
            .bucket(BucketItem::new)
            .build()
            .register();

    public static Fluid getSludge() {
        return SLUDGE.get();
    }

    public static void init() {}
}
