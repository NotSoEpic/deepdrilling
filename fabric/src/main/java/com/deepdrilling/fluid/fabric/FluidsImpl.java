package com.deepdrilling.fluid.fabric;

import com.deepdrilling.DrillMod;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

import static net.minecraft.world.item.Items.BUCKET;

@SuppressWarnings("UnstableApiUsage")
public class FluidsImpl {
    public static final FluidEntry<SimpleFlowableFluid.Flowing> SLUDGE = DrillMod.REGISTRATE
            .fluid("sludge", DrillMod.id("block/sludge_still"), DrillMod.id("block/sludge_flow"), SimpleFlowableFluid.Flowing::new)
//            .standardFluid("sludge")
            .lang("Sludge")
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(30)
                    .blastResistance(100))
            .fluidAttributes(() -> new FluidAttributeHandler("block.deepdrilling.sludge", 2000, 1400))
            .tag(FluidTags.WATER)
            .source(SimpleFlowableFluid.Source::new)
            .bucket()
            .build()
            .onRegisterAfter(
                Registries.ITEM, sludge -> {
                    Fluid source = sludge.getSource();
                    // transfer values
                    FluidStorage.combinedItemApiProvider(source.getBucket()).register(context ->
                            new FullItemFluidStorage(context, bucket -> ItemVariant.of(BUCKET), FluidVariant.of(source), FluidConstants.BUCKET));

                    FluidStorage.combinedItemApiProvider(BUCKET).register(context ->
                            new EmptyItemFluidStorage(context, bucket -> ItemVariant.of(source.getBucket()), source, FluidConstants.BUCKET));
            })
            .register();

    public static Fluid getSludge() {
        return SLUDGE.get();
    }

    public static void init() {}

    private record FluidAttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private FluidAttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        public FluidAttributeHandler(String key) {
            this(key, FluidConstants.WATER_VISCOSITY, 1000);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name.copy();
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }
    }
}
