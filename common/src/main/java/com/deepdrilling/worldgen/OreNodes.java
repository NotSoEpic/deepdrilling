package com.deepdrilling.worldgen;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.simibubi.create.AllBlocks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

public class OreNodes {
    public static final ResourceLocation ORE_NODE_ID = DrillMod.id("ore_node");
    public static final OreNodeFeature ORE_NODE = new OreNodeFeature(OreNodeConfiguration.CODEC);

    public static final TagKey<Biome> ASURINE_NODE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, DrillMod.id("asurine_node"));
    public static final TagKey<Biome> CRIMSITE_NODE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, DrillMod.id("crimsite_node"));
    public static final TagKey<Biome> OCHRUM_NODE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, DrillMod.id("ochrum_node"));
    public static final TagKey<Biome> VERIDIUM_NODE_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, DrillMod.id("veridium_node"));

    public static void init() {
        register(new ConfiguredNodeBuilder(DBlocks.ASURINE_NODE)
                .addOre(AllBlocks.DEEPSLATE_ZINC_ORE)
                .addLayer(new ResourceLocation("create", "asurine"))
                .addLayer(Blocks.TUFF)
                .addLayer(Blocks.CALCITE)
                .configureFeature(200), DrillMod.id("asurine_node"), ASURINE_NODE_BIOMES);
        register(new ConfiguredNodeBuilder(DBlocks.CRIMSITE_NODE)
                .addOre(Blocks.DEEPSLATE_IRON_ORE)
                .addLayer(new ResourceLocation("create", "crimsite"))
                .addLayer(Blocks.TUFF)
                .addLayer(new ResourceLocation("create", "limestone"))
                .configureFeature(150), DrillMod.id("crimsite_node"), CRIMSITE_NODE_BIOMES);
        register(new ConfiguredNodeBuilder(DBlocks.OCHRUM_NODE)
                .addOre(Blocks.DEEPSLATE_GOLD_ORE)
                .addLayer(new ResourceLocation("create", "ochrum"))
                .addLayer(Blocks.TUFF)
                .addLayer(Blocks.DRIPSTONE_BLOCK)
                .configureFeature(350), DrillMod.id("ochrum_node"), OCHRUM_NODE_BIOMES);
        register(new ConfiguredNodeBuilder(DBlocks.VERIDIUM_NODE)
                .addOre(Blocks.DEEPSLATE_COPPER_ORE)
                .addLayer(new ResourceLocation("create", "veridium"))
                .addLayer(Blocks.TUFF)
                .addLayer(Blocks.ANDESITE)
                .configureFeature(250), DrillMod.id("veridium_node"), VERIDIUM_NODE_BIOMES);

    }
    @ExpectPlatform
    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation name, TagKey<Biome> filterTag) {
        throw new AssertionError();
    }
}
