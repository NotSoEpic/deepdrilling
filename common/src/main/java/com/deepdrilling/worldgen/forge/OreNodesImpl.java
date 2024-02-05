package com.deepdrilling.worldgen.forge;

import com.deepdrilling.worldgen.ConfiguredNodeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;

public class OreNodesImpl {
    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation name) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, data.configured());
        Registry.register(BuiltinRegistries.PLACED_FEATURE, name, data.placed());

        // todo: not use a json file for this
    }
}
