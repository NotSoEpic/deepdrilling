package com.deepdrilling.worldgen.fabric;

import com.deepdrilling.worldgen.ConfiguredNodeBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class OreNodesImpl {
    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation name) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, data.configured());
        Registry.register(BuiltinRegistries.PLACED_FEATURE, name, data.placed());

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, name)
        );
    }
}
