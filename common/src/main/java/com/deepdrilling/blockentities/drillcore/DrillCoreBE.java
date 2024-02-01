package com.deepdrilling.blockentities.drillcore;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.IDrillCollector;
import com.deepdrilling.blockentities.IDrillDamageMod;
import com.deepdrilling.blockentities.IDrillSpeedMod;
import com.deepdrilling.blockentities.IModule;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import com.deepdrilling.nodes.OreNode;
import com.deepdrilling.nodes.OreNodes;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/** lots of this is taken from {@link BlockBreakingKineticBlockEntity} */
public class DrillCoreBE extends KineticBlockEntity {
    protected int breakerId = -BlockBreakingKineticBlockEntity.NEXT_BREAKER_ID.incrementAndGet();
    protected int ticksUntilProgress; // increments destroyProgress at 0, is -1 if not progressing at all
    protected int destroyProgress;
    protected BlockPos breakingPos;

    // (distance from drill, interface instance)
    protected List<IModule> modules = new ArrayList<>();
    protected List<Tuple<Integer, IDrillSpeedMod>> speedModifiers = new ArrayList<>();
    protected List<Tuple<Integer, IDrillCollector>> collectors = new ArrayList<>();
    protected List<Tuple<Integer, IDrillDamageMod>> drillDamagers = new ArrayList<>();

    public DrillCoreBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public boolean isStalled() {
        return ticksUntilProgress < 0;
    }

    public void progressNextTick() {
        ticksUntilProgress = 1;
    }

    public BlockPos getDrillHeadPosition() {
        return getBlockPos().relative(getBlockState().getValue(com.deepdrilling.blocks.DrillCore.FACING));
    }

    public BlockPos getBreakingPosition() {
        return getBlockPos().relative(getBlockState().getValue(com.deepdrilling.blocks.DrillCore.FACING), 2);
    }

    public void ifDrillHeadDo(Consumer<DrillHeadBE> lambda) {
        DrillHeadBE drillHead = getDrillHead();
        if (drillHead != null) {
            lambda.accept(drillHead);
        }
    }

    // ticks per mining operation (will be affected by rounding)
    public double calculateSpeed() {
        if (getSpeed() == 0)
            return -1;
        // 10 seconds at 256 rpm, 20s at 64, 40s at 16
        AtomicReference<Double> baseSpeed = new AtomicReference<>((20 * 10) / Math.sqrt(Math.abs(getSpeed() / 256f)));
        ifDrillHeadDo(drillHead -> baseSpeed.set(baseSpeed.get() * drillHead.getSpeedModifier()));
        double finalSpeed = baseSpeed.get();
        for (Tuple<Integer, IDrillSpeedMod> speedMod : speedModifiers) {
            finalSpeed = speedMod.getB().modifySpeed(baseSpeed.get(), finalSpeed);
        }
        return finalSpeed;
    }

    public double calculateDamage() {
        double damage = 1;
        for (Tuple<Integer, IDrillDamageMod> damageMod : drillDamagers) {
            damage = damageMod.getB().modifyDamage(damage);
        }
        return Math.max(damage, 0);
    }

    public int ticksPerProgress() {
        double speed = calculateSpeed();
        if (speed < 0)
            return -1;
        return (int) Mth.clamp(0.1 * speed, 1, 160);
    }

    public DrillHeadBE drillHead = null;
    @Nullable
    public DrillHeadBE getDrillHead() {
        if (drillHead == null && level.getBlockEntity(getDrillHeadPosition()) instanceof DrillHeadBE levelDrillHead) {
            drillHead = levelDrillHead;
        }
        return drillHead;
    }

    public void removeDrillHead() {
        drillHead = null;
    }

    public boolean canDrill(BlockState state) {
        return !state.getMaterial().isLiquid() &&
                !state.isAir() &&
                getDrillHead() != null;
    }

    public List<ItemStack> getDrops() {
        OreNode node = OreNodes.get(level.getBlockState(breakingPos).getBlock());
        DrillHeadStats.WeightMultipliers weights = drillHead.getWeightMultipliers().mul(node.weights);
        OreNode.LOOT_TYPE type = weights.pick(level.random);

        LootTable loot = node.getTable(level.getServer().getLootTables(), type);
        LootContext.Builder builder = new LootContext.Builder((ServerLevel) level);
        return loot.getRandomItems(builder.create(LootContextParamSets.EMPTY));
    }

    public void mineBlock() {
        findModules();

        List<ItemStack> drops = getDrops();

        for (Tuple<Integer, IDrillCollector> collectorInfo : collectors) {
            IDrillCollector collector = collectorInfo.getB();
            drops = collector.collectItems(drops);
            if (drops == null || drops.isEmpty())
                return;
        }
        for (ItemStack drop : drops) {
            ItemEntity item = new ItemEntity(level,
                    getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
                    drop
            );
            level.addFreshEntity(item);
        }
    }

    public static int searchDist = 5;
    public void findModules() {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.set(getBlockPos());
        Direction dir = getBlockState().getValue(com.deepdrilling.blocks.DrillCore.FACING).getOpposite();

        modules.clear();
        speedModifiers.clear();
        collectors.clear();
        drillDamagers.clear();

        for (int i = 0; i < searchDist; i++) {
            pos.move(dir);
            BlockEntity candidate = level.getBlockEntity(pos);

            if (candidate instanceof IModule module &&
                    module.getAxis() == getBlockState().getValue(com.deepdrilling.blocks.DrillCore.FACING).getAxis()) {
                modules.add(module);
                if (candidate instanceof IDrillSpeedMod speedMod) {
                    speedModifiers.add(new Tuple<>(i, speedMod));
                }
                if (candidate instanceof IDrillCollector collector) {
                    collectors.add(new Tuple<>(i, collector));
                }
                if (candidate instanceof IDrillDamageMod damager) {
                    drillDamagers.add(new Tuple<>(i, damager));
                }
            } else {
                break;
            }
        }

        speedModifiers.sort(IDrillSpeedMod.drillCollectComparator);
        collectors.sort(IDrillCollector.drillCollectComparator);
        drillDamagers.sort(IDrillDamageMod.drillDamageComparator);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean val = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (getSpeed() != 0) {
            int totalTicks = ticksPerProgress() * 10;
            Lang.text("Drilling every:").style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            String time = LangNumberFormat.format(totalTicks / 20f);
            Lang.builder().space()
                    .text(time)
                        .style(ChatFormatting.AQUA)
                    .add(Lang.text(Objects.equals(time, "1") ? " second" : " seconds")
                        .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip);
            val = true;
        }
        if (!modules.isEmpty() && isPlayerSneaking) {
            Lang.text("Attached Modules:")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            for (IModule module : modules) {
                Lang.builder().space()
                        .add(module.getName())
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);
            }
            val = true;
        }

        return val;
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (isStalled())
            progressNextTick();
        ticksUntilProgress = Math.min(ticksUntilProgress, ticksPerProgress());
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (isStalled())
            progressNextTick();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!level.isClientSide && destroyProgress != 0)
            level.destroyBlockProgress(breakerId, breakingPos, -1);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide || getSpeed() == 0 || ticksUntilProgress < 0)
            return;

        breakingPos = getBreakingPosition();
        BlockState breakingState = level.getBlockState(breakingPos);

        if (!canDrill(breakingState)) {
            if (destroyProgress > 0) {
                destroyProgress = 0;
                level.destroyBlockProgress(breakerId, breakingPos, -1);
            }
            return;
        }

        if (ticksUntilProgress-- == 0) {
            ticksUntilProgress = ticksPerProgress();
            destroyProgress++;
            modules.forEach(m -> m.progressBreaking(this));
            if (destroyProgress >= 10) {
                destroyProgress = 0;
                modules.forEach(m -> m.blockBroken(this));
                mineBlock();
                ifDrillHeadDo(drillHead -> drillHead.applyDamage(calculateDamage()));
                level.destroyBlockProgress(breakerId, breakingPos, -1);
            } else {
                level.destroyBlockProgress(breakerId, breakingPos, destroyProgress);
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Progress", destroyProgress);
        compound.putInt("NextTick", ticksUntilProgress);
        if (breakingPos != null)
            compound.put("Breaking", NbtUtils.writeBlockPos(breakingPos));
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        destroyProgress = compound.getInt("Progress");
        ticksUntilProgress = compound.getInt("NextTick");
        if (compound.contains("Breaking"))
            breakingPos = NbtUtils.readBlockPos(compound.getCompound("Breaking"));
        super.read(compound, clientPacket);
    }
}
