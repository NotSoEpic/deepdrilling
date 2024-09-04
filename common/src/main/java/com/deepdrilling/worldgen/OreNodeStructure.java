package com.deepdrilling.worldgen;

import com.deepdrilling.DrillMod;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.List;
import java.util.Optional;

public class OreNodeStructure extends Structure {
    public static final Codec<OreNodeStructure> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    settingsCodec(instance),
                    Data.CODEC.fieldOf("ore_node").forGetter(node -> node.data)
            ).apply(instance, OreNodeStructure::new)
    );

    public OreNodeStructure(Structure.StructureSettings settings, Data data) {
        super(settings);
        this.data = data;
    }

    private final Data data;

    // todo find out why its consistently offset by 1-2 chunks
    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        BlockPos pos = context.chunkPos().getBlockAt(
                context.random().nextInt(16),
                context.heightAccessor().getMinBuildHeight(),
                context.random().nextInt(16)
        );
        OreNodePiece piece = new OreNodePiece(pos.getX(), pos.getY(), pos.getZ(), data);
        return Optional.of(new GenerationStub(
                pos,
                builder -> builder.addPiece(piece)
        ));
    }

    private static final StructureType<OreNodeStructure> TYPE =
            Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DrillMod.id("ore_node"), () -> CODEC);
    @Override
    public StructureType<?> type() {
        return TYPE;
    }

    record Data(ResourceLocation node, List<ResourceLocation> ores, List<ResourceLocation> layers) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        ResourceLocation.CODEC.fieldOf("nodeID").forGetter(Data::node),
                        Codec.list(ResourceLocation.CODEC).fieldOf("ores").forGetter(Data::ores),
                        Codec.list(ResourceLocation.CODEC).fieldOf("layers").forGetter(Data::layers)
                ).apply(instance, Data::new)
        );
    }

    public static void init() {
        OreNodePiece.init();
    }
}
