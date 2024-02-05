package com.deepdrilling.worldgen;

import com.mojang.math.Vector3d;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;

public class OreNodeFeature extends Feature<OreNodeConfiguration> {
    public OreNodeFeature(Codec<OreNodeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreNodeConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        OreNodeConfiguration config = context.config();

        BlockState blockState = parseResourceLocation(config.node());

        List<ResourceLocation> oreIDs = config.ores();
        if (oreIDs.isEmpty()) throw new IllegalArgumentException("No identifiers for ores list");
        List<BlockState> oreStates = oreIDs.stream().map(OreNodeFeature::parseResourceLocation).toList();

        List<ResourceLocation> layerIDs = config.layers();
        if (layerIDs.isEmpty()) throw new IllegalArgumentException("No identifiers for layers list");
        List<BlockState> layerStates = layerIDs.stream().map(OreNodeFeature::parseResourceLocation).toList();

        List<BlockPos> blobCenters = new ArrayList<>();
        for (int i = random.nextInt(3, 4); i > 0; i--) {
            blobCenters.add(new BlockPos(
                    random.nextInt(-6, 6),
                    random.nextInt(0, 6),
                    random.nextInt(-6, 6)));
        }
        blobCenters.add(BlockPos.ZERO);

        Vector3d blobCom = new Vector3d(0, 0, 0);
        blobCenters.forEach(pos -> blobCom.add(new Vector3d(pos.getX(), pos.getY(), pos.getZ())));
        blobCom.scale(1.0 / blobCenters.size());

        int offs = random.nextInt(layerStates.size());

        // main ore
        for (int x = -16; x < 17; x++) {
            for (int z = -16; z < 17; z++) {
                for (int y = 0; y < 35; y++) {
                    BlockPos pos = new BlockPos(x + origin.getX(), y + level.getMinBuildHeight(), z + origin.getZ());
                    if (y == 0) {
                        // place ore nodes at bedrock
                        double dx = x - blobCom.x;
                        double dz = z - blobCom.z;
                        if (dx*dx + dz*dz <= 6) {
                            level.setBlock(pos, blockState, 0x10);
                        }
                    } else {
                        if (lengthSquared(new Vector3d(blobCom.x-x, blobCom.y-y, blobCom.z-z)) < 16) {
                            level.setBlock(pos, y < 4 ? Blocks.LAVA.defaultBlockState() : Blocks.AIR.defaultBlockState(), 0x10);
                        } else {
                            double r = 0;
                            for (BlockPos blobPos : blobCenters) {
                                Vector3d diff = vec3FromBlock(blobPos);
                                diff.add(new Vector3d(-x, -y * 0.65, -z));
                                r += Math.sqrt(lengthSquared(diff));
                            }
                            r /= blobCenters.size();
                            r += random.nextDouble() * 0.5;
                            // "distance" from center of blobs
                            int idx = (int) (r);
                            // layers of shell
                            if (idx < 10) {
                                idx = (idx + offs) % layerStates.size();
                                level.setBlock(pos, layerStates.get(idx), 0x10);
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
                                state -> state.is(Blocks.AIR) || !state.getFluidState().isEmpty() || state.is(BlockTags.REPLACEABLE_PLANTS) || state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES));
                    }
                }
            }
        }
        return true;
    }

    public static BlockState parseResourceLocation(ResourceLocation resource) {
        BlockState blockState = Registry.BLOCK.get(resource).defaultBlockState();
        if (blockState == null) throw new IllegalArgumentException(resource + " could not be parsed as a valid block identifier");
        return blockState;
    }

    public static <T> T choose(List<T> list, RandomSource random) {
        return list.get(random.nextInt(list.size()));
    }

    public static Vector3d vec3FromBlock(BlockPos pos) {
        return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static double lengthSquared(Vector3d vec) {
        return (vec.x * vec.x) + (vec.y * vec.y) + (vec.z * vec.z);
    }
}
