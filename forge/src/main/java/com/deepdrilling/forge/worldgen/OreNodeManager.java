package com.deepdrilling.forge.worldgen;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.deepdrilling.forge.DrillModForge;
import com.deepdrilling.nodes.ConfiguredNodeBuilder;
import com.deepdrilling.nodes.OreNodeCommon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class OreNodeManager {
    public static RegistryObject<Codec<OreNodeBiomeModifier>> EXAMPLE_CODEC = DrillModForge.BIOME_MODIFIER_SERIALIZERS.register("example", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    // declare fields
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(OreNodeBiomeModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(OreNodeBiomeModifier::feature)
                    // declare constructor
            ).apply(builder, OreNodeBiomeModifier::new)));

    public static ConfiguredNodeBuilder.FeatureData ASURINE = new ConfiguredNodeBuilder(DBlocks.ASURINE_NODE)
            .addOre(AllBlocks.ZINC_ORE)
            .addLayer(new ResourceLocation("create", "asurine"))
            .configureFeature();

    public static void registerStuff(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.FEATURES, helper -> {
            helper.register(OreNodeCommon.ORE_NODE_ID, OreNodeCommon.ORE_NODE);
        });
        event.register(ForgeRegistries.Keys.);
    }
}
