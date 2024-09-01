package com.deepdrilling.blockentities.drillcore;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.Truple;
import com.deepdrilling.blockentities.ModuleBE;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import com.deepdrilling.blockentities.module.Module;
import com.deepdrilling.blocks.DrillCore;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/** lots of this is taken from {@link BlockBreakingKineticBlockEntity} */
public class DrillCoreBE extends KineticBlockEntity {
    private final int breakerId = -BlockBreakingKineticBlockEntity.NEXT_BREAKER_ID.incrementAndGet();
    private int ticksUntilProgress; // increments destroyProgress at 0, is -1 if not progressing at all
    private int destroyProgress;
    private BlockPos breakingPos;

    // (distance from drill, interface instance)
    private List<Module> modules = new ArrayList<>();
    // { modifierType: [(distance from core, associated BE, modifier)] }
    private HashMap<Modifier.Type, List<Truple<Integer, BlockEntity, Modifier>>> modifiers = new HashMap<>();

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

    // this is awful :3
    public <T> T applyModifiers(T baseValue, Modifier.Type<T> type) {
        T value = baseValue;
        for (Truple<Integer, BlockEntity, Modifier> modifier : modifiers.getOrDefault(type, List.of())) {
            value = (T) modifier.getC().modifier.apply(this, getDrillHead(), modifier.getB(), value, baseValue);
        }
        return value;
    }


    // ticks per mining operation (will be affected by rounding)
    public double calculateSpeed() {
        if (getSpeed() == 0)
            return -1;
        // 10 seconds at 256 rpm, 20s at 64, 40s at 16
        AtomicReference<Double> baseSpeed = new AtomicReference<>((20 * 10) / Math.sqrt(Math.abs(getSpeed() / 256f)));
        ifDrillHeadDo(drillHead -> baseSpeed.set(baseSpeed.get() * drillHead.getSpeedModifier()));
        return applyModifiers(baseSpeed.get(), ModifierTypes.SPEED);
    }

    public double calculateDamage() {
        return Math.max(applyModifiers(1d, ModifierTypes.DAMAGE), 0);
    }

    public DrillHeadStats.WeightMultipliers getWeightMultipliers() {
        return applyModifiers(DrillHeadStats.WeightMultipliers.ONE, ModifierTypes.RESOURCE_WEIGHT);
    }

    public int ticksPerProgress() {
        double speed = calculateSpeed();
        if (speed < 0)
            return -1;
        return (int) Mth.clamp(0.05 * speed, 1, 160);
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
        return !state.liquid() &&
                !state.isAir() &&
                getDrillHead() != null &&
                getDrillHead().isFunctional() &&
                OreNodes.get(state.getBlock()).hasTables();
    }

    public List<ItemStack> getDrops(ServerLevel level) {
        OreNode node = OreNodes.get(level.getBlockState(breakingPos).getBlock());
        DrillHeadStats.WeightMultipliers weights = drillHead.getWeightMultipliers().mul(node.weights);
        weights = weights.mul(getWeightMultipliers());
        OreNode.LOOT_TYPE type = weights.pick(level.random);

        LootTable lootTable = node.getTable(level, type);
        // todo: proper lootcontextparam
        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(getBreakingPosition()))
                .create(LootContextParamSets.ARCHAEOLOGY);
        List<ItemStack> drops = new ArrayList<>();
        double dropCount = drillHead.getFortuneAmount() + 1;
        while (dropCount-- > level.random.nextFloat()) {
            drops.addAll(lootTable.getRandomItems(lootParams));
        }
        return drops;
    }

    public void mineBlock(ServerLevel level) {
        findModules();

        List<ItemStack> drops = applyModifiers(getDrops(level), ModifierTypes.OUTPUT_LIST);

        for (ItemStack drop : drops) {
            ItemEntity item = new ItemEntity(level,
                    getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5,
                    drop
            );
            level.addFreshEntity(item);
        }
    }

    public static int searchDist = 5;
    public void findModules() {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.set(getBlockPos());
        Direction dir = getBlockState().getValue(DrillCore.FACING).getOpposite();

        modules.clear();
        modifiers.clear();

        Set<ResourceLocation> uniqueNames = new HashSet<>();

        for (int zingusValue = 0; zingusValue < searchDist; zingusValue++) {
            pos.move(dir);
            BlockEntity candidate = level.getBlockEntity(pos);

            if (candidate instanceof ModuleBE module &&
                    module.getAxis() == getBlockState().getValue(com.deepdrilling.blocks.DrillCore.FACING).getAxis()) {
                if (Collections.disjoint(uniqueNames, module.getMutuallyExclusiveNames())) {
                    uniqueNames.addAll(module.getMutuallyExclusiveNames());

                    modules.add(module);
                    for (Modifier modifier : module.getModifiers()) {
                        modifiers.computeIfAbsent(modifier.type, k -> new ArrayList<>())
                                .add(new Truple<>(zingusValue, module, modifier));
                    }
                }
            }
        }

        for (List<Truple<Integer, BlockEntity, Modifier>> modifierInstances : modifiers.values()) {
            modifierInstances.sort(Modifier.modifierComparator);
        }
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
            for (Module module : modules) {
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

        if (!(level instanceof ServerLevel serverLevel) || getSpeed() == 0 || ticksUntilProgress < 0)
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
                mineBlock(serverLevel);
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
