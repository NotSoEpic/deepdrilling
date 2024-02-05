package com.deepdrilling.nodes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record OreNodeConfiguration(ResourceLocation node, List<ResourceLocation> ores, List<ResourceLocation> layers) implements FeatureConfiguration {
    public static final Codec<OreNodeConfiguration> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("nodeID").forGetter(OreNodeConfiguration::node),
                    Codec.list(ResourceLocation.CODEC).fieldOf("ores").forGetter(OreNodeConfiguration::ores),
                    Codec.list(ResourceLocation.CODEC).fieldOf("layers").forGetter(OreNodeConfiguration::layers)
            ).apply(instance, OreNodeConfiguration::new)
    );
}
