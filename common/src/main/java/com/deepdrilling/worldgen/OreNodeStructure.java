package com.deepdrilling.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

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

    @Override
    public StructureType<?> type() {
        return getStructureType();
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

    @ExpectPlatform
    public static StructureType<OreNodeStructure> getStructureType() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static StructurePieceType getPieceType() {
        throw new AssertionError();
    }
}
