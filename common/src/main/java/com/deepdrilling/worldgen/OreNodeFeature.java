package com.deepdrilling.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

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

        ResourceLocation nodeID = config.node();
        BlockState blockState = Registry.BLOCK.get(nodeID).defaultBlockState();
        if (blockState == null) throw new IllegalArgumentException(nodeID + " could not be parsed as a valid block identifier");

        BlockPos testPos = new BlockPos(origin);
        for (int y = 0; y < level.getHeight(); y++) {
            testPos = testPos.above();
            if (level.getBlockState(testPos).is(BlockTags.DIRT)) {
                if (level.getBlockState(testPos.above()).is(Blocks.AIR)) {
                    for (int i = 0; i < 5; i++) {
                        testPos = testPos.above();
                        level.setBlock(testPos, blockState, 0x10);
                        if (testPos.getY() >= level.getHeight()) break;
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
