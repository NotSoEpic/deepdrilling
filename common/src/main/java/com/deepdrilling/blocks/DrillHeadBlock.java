package com.deepdrilling.blocks;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import com.simibubi.create.AllShapes;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Predicate;

public class DrillHeadBlock extends DirectionalKineticBlock implements IBE<DrillHeadBE> {

    VoxelShaper SHAPE = new AllShapes.Builder(
            Block.box(1, -1, 1, 15, 17, 15))
            .forDirectional();

    public static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());
    public static final ResourceLocation DRILL_DURABILITY = DrillMod.id("drill_durability");
    public DrillHeadBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof DrillHeadBE drillHeadBE) {
            builder = builder.withDynamicDrop(DRILL_DURABILITY, (context, consumer) -> {
                consumer.accept(drillHeadBE.setItemDamage(state.getBlock().asItem().getDefaultInstance()));
            });
        }
        return super.getDrops(state, builder);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);

        withBlockEntityDo(worldIn, pos, (drillHeadBE) -> drillHeadBE.applyItemDamage(stack));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
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
    public Class<DrillHeadBE> getBlockEntityClass() {
        return DrillHeadBE.class;
    }

    @Override
    public BlockEntityType<DrillHeadBE> getBlockEntityType() {
        return DDrillHeads.DRILL_HEAD_BE.get();
    }

    // todo: figure out how the fuck blocks like shafts and cogs render
    //  seriously - I had to do a lot more tomfoolery than create seems to do and
    //  the PlacementHelper ghost doesn't even render at all
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

        @Override
        public void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
            if (!offset.hasGhostState())
                return;
            CreateClient.GHOST_BLOCKS.showGhostState(this, offset.getTransform().apply(offset.getGhostState()))
                    .at(offset.getBlockPos())
                    .breathingAlpha();
        }
    }
}
