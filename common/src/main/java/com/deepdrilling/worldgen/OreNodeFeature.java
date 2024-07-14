package com.deepdrilling.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

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

        Vec3 blobCom = new Vec3(0, 0, 0);
        blobCenters.forEach(pos -> blobCom.add(Vec3.atCenterOf(pos)));
        blobCom.scale(1.0 / blobCenters.size());

        int offs = random.nextInt(layerStates.size());

        // todo what the fuck does /place do to create phantom blocks
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
                        if (blobCom.subtract(x, y, z).lengthSqr() < 16) {
                            level.setBlock(pos, y < 4 ? Blocks.LAVA.defaultBlockState() : Blocks.AIR.defaultBlockState(), 0x10);
                        } else {
                            double r = 0;
                            for (BlockPos blobPos : blobCenters) {
                                Vec3 diff = Vec3.atCenterOf(blobPos);
                                diff.add(new Vec3(-x, -y * 0.65, -z));
                                r += diff.length();
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
                                state -> state.is(Blocks.AIR) || !state.getFluidState().isEmpty() || state.is(BlockTags.REPLACEABLE) || state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) || state.is(BlockTags.AZALEA_ROOT_REPLACEABLE));
                    }
                }
            }
        }
        return true;
    }

    public static BlockState parseResourceLocation(ResourceLocation resource) {
        return BuiltInRegistries.BLOCK.get(resource).defaultBlockState();
    }

    public static <T> T choose(List<T> list, RandomSource random) {
        return list.get(random.nextInt(list.size()));
    }
}
