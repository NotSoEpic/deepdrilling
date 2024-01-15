package com.deepdrilling.blocks;

import com.deepdrilling.DBlocks;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Predicate;

public class DrillHeadBlock extends DirectionalKineticBlock implements IBE<DrillHeadBE> {
    public static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());
    public DrillHeadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockPos drillPos = pos.relative(dir.getOpposite());
        BlockState attached = level.getBlockState(drillPos);
        return super.canSurvive(state, level, pos) &&
                attached.is(DBlocks.DRILL.get()) &&
                attached.getValue(DrillCore.FACING) == dir;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        Direction blockFacing = state.getValue(FACING);
        if (!level.isClientSide &&
                fromPos.equals(pos.relative(blockFacing.getOpposite())) &&
                !canSurvive(state, level, pos)
        ) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (!canSurvive(state, context.getLevel(), context.getClickedPos()))
            return null;
        return state;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);

        ItemStack heldItem = player.getItemInHand(hand);
        if (player.mayBuild() && helper.matchesItem(heldItem))
            return helper.getOffset(player, level, state, pos, hit)
                    .placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, hit);
        return InteractionResult.PASS;
    }

    @Override
    public Class<DrillHeadBE> getBlockEntityClass() {
        return DrillHeadBE.class;
    }

    @Override
    public BlockEntityType<DrillHeadBE> getBlockEntityType() {
        return DDrillHeads.DRILL_HEAD_BE.get();
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> (i.getItem() instanceof BlockItem bi &&
                    bi.getBlock() instanceof DrillHeadBlock);
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof DrillCore;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction dir = state.getValue(DrillCore.FACING);
            if (world.getBlockState(pos.relative(dir)).getMaterial().isReplaceable())
                return PlacementOffset.success(pos.relative(dir), s -> s.setValue(DrillHeadBlock.FACING, dir));
            return PlacementOffset.fail();
        }
    }
}
