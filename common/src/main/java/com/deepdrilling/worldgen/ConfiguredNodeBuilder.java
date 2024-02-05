package com.deepdrilling.worldgen;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.ArrayList;
import java.util.List;

public class ConfiguredNodeBuilder {
    public record FeatureData(ConfiguredFeature<OreNodeConfiguration, OreNodeFeature> configured, PlacedFeature placed) {}

    private final ResourceLocation oreNode;
    private final List<ResourceLocation> ores = new ArrayList<>();
    private final List<ResourceLocation> layers = new ArrayList<>();
    public ConfiguredNodeBuilder(ResourceLocation oreNode) {
        this.oreNode = oreNode;
    }
    public ConfiguredNodeBuilder(BlockEntry<?> oreNode) {
        this.oreNode = oreNode.getId();
    }

    public ConfiguredNodeBuilder addOre(ResourceLocation ore) {
        this.ores.add(ore);
        return this;
    }
    public ConfiguredNodeBuilder addOre(Block ore) {
        this.ores.add(Registry.BLOCK.getKey(ore));
        return this;
    }
    public ConfiguredNodeBuilder addOre(BlockEntry<?> ore) {
        return addOre(ore.getId());
    }
    public ConfiguredNodeBuilder addLayer(ResourceLocation layers) {
        this.layers.add(layers);
        return this;
    }
    public ConfiguredNodeBuilder addLayer(BlockEntry<?> layer) {
        return addLayer(layer.getId());
    }
    public ConfiguredNodeBuilder addLayer(NonNullSupplier<Block> layer) {
        return addLayer(Registry.BLOCK.getKey(layer.get()));
    }
    public ConfiguredNodeBuilder addLayer(Block layer) {
        return addLayer(Registry.BLOCK.getKey(layer));
    }

    public FeatureData configureFeature(List<PlacementModifier> filters) {
        ConfiguredFeature<OreNodeConfiguration, OreNodeFeature> CONFIGURED_FEATURE =
                new ConfiguredFeature<>(
                        OreNodes.ORE_NODE, new OreNodeConfiguration(oreNode, ores, layers)
                );

        PlacedFeature PLACED_FEATURE = new PlacedFeature(
                Holder.direct(CONFIGURED_FEATURE), filters
        );
        return new FeatureData(CONFIGURED_FEATURE, PLACED_FEATURE);
    }

    public FeatureData configureFeature(int odds) {
        return configureFeature(List.of(RarityFilter.onAverageOnceEvery(odds), InSquarePlacement.spread()));
    }
}
