package com.deepdrilling.fabric;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.ConfiguredNodeBuilder;
import com.deepdrilling.nodes.OreNodeCommon;
import com.simibubi.create.AllBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;

public class OreNodeManager {

    public static void init() {
        Registry.register(Registry.FEATURE, OreNodeCommon.ORE_NODE_ID, OreNodeCommon.ORE_NODE);

        register(new ConfiguredNodeBuilder(DBlocks.ASURINE_NODE)
                .addOre(AllBlocks.ZINC_ORE)
                .addLayer(new ResourceLocation("create", "asurine"))
                .configureFeature(), DrillMod.id("asurine_node"));
        register(new ConfiguredNodeBuilder(DBlocks.CRIMSITE_NODE)
                .addOre(Blocks.IRON_ORE)
                .addLayer(new ResourceLocation("create", "asurine"))
                .configureFeature(), DrillMod.id("crimsite_node"));

    }

    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation resource) {

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, resource, data.configured());
        Registry.register(BuiltinRegistries.PLACED_FEATURE, resource, data.placed());

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, resource)
        );
    }
}
