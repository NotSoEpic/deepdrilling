package com.deepdrilling.worldgen.forge;

import com.deepdrilling.worldgen.ConfiguredNodeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class OreNodesImpl {
    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation name, TagKey<Biome> filterTag) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, data.configured());
        Registry.register(BuiltinRegistries.PLACED_FEATURE, name, data.placed());

        // todo: not use a json file for this
    }
}
