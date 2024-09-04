package com.deepdrilling.worldgen;

import com.deepdrilling.DrillMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OreNodePiece extends StructurePiece {
    private final OreNodeStructure.Data data;
    public OreNodePiece(int x, int y, int z, OreNodeStructure.Data data) {
        super(OreNodeStructure.getPieceType(), y, BoundingBox.orientBox(x, y, z, 0, 0, 0, 32, 32, 32, Direction.SOUTH));
        this.data = data;
    }

    public OreNodePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super(OreNodeStructure.getPieceType(), compoundTag);
        this.data = OreNodeStructure.Data.CODEC.decode(NbtOps.INSTANCE, compoundTag.get("node_data"))
                .resultOrPartial(str -> DrillMod.LOGGER.error("Error loading ore node save data: {}", str)).orElseThrow().getFirst();
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        OreNodeStructure.Data.CODEC.encodeStart(NbtOps.INSTANCE, data)
                .resultOrPartial(str -> DrillMod.LOGGER.error("Error adding ore node save data: {}", str)).ifPresent(
                        codecTag -> tag.put("node_data", codecTag)
                );
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos origin) {
        if (!chunkPos.equals(new ChunkPos(origin)))
            return;

        BlockState blockState = parseResourceLocation(data.node());

        List<ResourceLocation> oreIDs = data.ores();
        if (oreIDs.isEmpty()) throw new IllegalArgumentException("No identifiers for ores list");
        List<BlockState> oreStates = oreIDs.stream().map(OreNodePiece::parseResourceLocation).toList();

        List<ResourceLocation> layerIDs = data.layers();
        if (layerIDs.isEmpty()) throw new IllegalArgumentException("No identifiers for layers list");
        List<BlockState> layerStates = layerIDs.stream().map(OreNodePiece::parseResourceLocation).toList();

        List<BlockPos> blobCenters = new ArrayList<>();
        for (int i = random.nextInt(3, 4); i > 0; i--) {
            blobCenters.add(new BlockPos(
                    random.nextInt(-6, 6),
                    random.nextInt(0, 6),
                    random.nextInt(-6, 6)));
        }
        blobCenters.add(BlockPos.ZERO);

        Vec3 blobCom = new Vec3(0, 0, 0);
        blobCenters.forEach(pos -> blobCom.add(Vec3.atCenterOf(pos)));
        blobCom.scale(1.0 / blobCenters.size());

        int offs = random.nextInt(layerStates.size());

        // main ore
        for (int x = -16; x < 17; x++) {
            for (int z = -16; z < 17; z++) {
                boolean hNearCom = false;
                for (int y = 0; y < 35; y++) {
                    BlockPos pos = new BlockPos(x + origin.getX(), y + level.getMinBuildHeight(), z + origin.getZ());
                    if (y == 0) {
                        // place ore nodes at bedrock
                        double dx = x - blobCom.x;
                        double dz = z - blobCom.z;
                        double comDist = dx*dx + dz*dz;
                        if (comDist <= 6) {
                            level.setBlock(pos, blockState, 0x10);
                        }
                        hNearCom = comDist <= 8;
                    } else {
                        if (blobCom.subtract(x, y, z).lengthSqr() < 16) {
                            level.setBlock(pos, y < 4 ? Blocks.LAVA.defaultBlockState() : Blocks.AIR.defaultBlockState(), 0x10);
                        } else {
                            double r = 0;
                            for (BlockPos blobPos : blobCenters) {
                                Vec3 diff = Vec3.atCenterOf(blobPos);
                                r += diff.add(-x, -y * 0.65, -z).length();
                            }
                            r /= blobCenters.size();
                            r += random.nextDouble() * 0.5;
                            // "distance" from center of blobs
                            int idx = (int) (r);
                            // layers of shell
                            if (idx < 10) {
                                idx = (idx + offs) % layerStates.size();
                                boolean finalHNearCom = hNearCom;
                                // some fucking how - using level.setBlock() creates phantom blocks with the /place command
                                // this still does it too, but less blatantly worse
                                safeSetBlock(level, pos, layerStates.get(idx),
                                        state -> finalHNearCom || !state.is(Blocks.BEDROCK));
                            }
                        }
                    }
                }
            }
        }

        // hint surface deposit
        BlockPos testPos = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, origin).below(random.nextInt(1, 3));
        for (int x = -3; x < 3; x++) {
            for (int z = -3; z < 3; z++) {
                for (int y = -3; y < 3; y++) {
                    BlockPos surfacePos = new BlockPos(x + testPos.getX(), y + testPos.getY(), z + testPos.getZ());
                    if (x*x + z*z + y*y < (random.nextFloat() * 4 + 5)) {
                        safeSetBlock(level, surfacePos,
                                choose(y < 0 ? oreStates : layerStates, random),
                                state -> state.is(Blocks.AIR) || !state.getFluidState().isEmpty() || state.is(BlockTags.REPLACEABLE) || state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) || state.is(BlockTags.AZALEA_ROOT_REPLACEABLE));
                    }
                }
            }
        }
    }

    private static BlockState parseResourceLocation(ResourceLocation resource) {
        return BuiltInRegistries.BLOCK.get(resource).defaultBlockState();
    }

    private static <T> T choose(List<T> list, RandomSource random) {
        return list.get(random.nextInt(list.size()));
    }

    private void safeSetBlock(WorldGenLevel level, BlockPos pos, BlockState state, Predicate<BlockState> oldState) {
        if (oldState.test(level.getBlockState(pos))) {
            level.setBlock(pos, state, 2);
        }

    }
}
