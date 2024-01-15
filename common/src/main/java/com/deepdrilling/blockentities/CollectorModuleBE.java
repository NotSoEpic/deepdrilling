package com.deepdrilling.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CollectorModuleBE extends ModuleBE implements IDrillCollector {
    public CollectorModuleBE(BlockEntityType typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public List<ItemStack> collectItems(List<ItemStack> items) {
        for (ItemStack drop : items) {
            ItemEntity item = new ItemEntity(level,
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 1.5, getBlockPos().getZ() + 0.5,
                    drop
            );
            level.addFreshEntity(item);
        }
        return new ArrayList<>();
    }
}
