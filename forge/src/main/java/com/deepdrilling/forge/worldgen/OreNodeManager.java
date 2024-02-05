package com.deepdrilling.forge.worldgen;

import com.deepdrilling.worldgen.OreNodeFeature;
import com.deepdrilling.worldgen.OreNodes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OreNodeManager {
    // i hate forge i hate forge i hate forge i hate forge
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, "deepdrilling");
    public static RegistryObject<OreNodeFeature> ORE_NODE_FEATURE = FEATURES.register(OreNodes.ORE_NODE_ID.getPath(), () -> OreNodes.ORE_NODE);
    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "deepdrilling");
    public static RegistryObject<Codec<OreNodeBiomeModifier>> ORE_NODE_CODEC = BIOME_MODIFIER_SERIALIZERS.register("ore_node", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    // declare fields
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(OreNodeBiomeModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(OreNodeBiomeModifier::feature)
                    // declare constructor
            ).apply(builder, OreNodeBiomeModifier::new)));

    public static void init(IEventBus eventBus) {
        FEATURES.register(eventBus);
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
