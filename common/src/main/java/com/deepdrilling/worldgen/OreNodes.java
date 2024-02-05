package com.deepdrilling.worldgen;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.simibubi.create.AllBlocks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class OreNodes {
    public static final ResourceLocation ORE_NODE_ID = DrillMod.id("ore_node");
    public static final OreNodeFeature ORE_NODE = new OreNodeFeature(OreNodeConfiguration.CODEC);
    public static void init() {
        register(new ConfiguredNodeBuilder(DBlocks.ASURINE_NODE)
                .addOre(AllBlocks.ZINC_ORE)
                .addLayer(new ResourceLocation("create", "asurine"))
                .configureFeature(), DrillMod.id("asurine_node"));
        register(new ConfiguredNodeBuilder(DBlocks.CRIMSITE_NODE)
                .addOre(Blocks.IRON_ORE)
                .addLayer(new ResourceLocation("create", "crimsite"))
                .configureFeature(), DrillMod.id("crimsite_node"));
        register(new ConfiguredNodeBuilder(DBlocks.OCHRUM_NODE)
                .addOre(Blocks.GOLD_ORE)
                .addLayer(new ResourceLocation("create", "ochrum"))
                .configureFeature(), DrillMod.id("ochrum_node"));
        register(new ConfiguredNodeBuilder(DBlocks.VERIDIUM_NODE)
                .addOre(Blocks.COPPER_ORE)
                .addLayer(new ResourceLocation("create", "veridium"))
                .configureFeature(), DrillMod.id("veridium_node"));

    }
    @ExpectPlatform
    public static void register(ConfiguredNodeBuilder.FeatureData data, ResourceLocation name) {
        throw new AssertionError();
    }
}
