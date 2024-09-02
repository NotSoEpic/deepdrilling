package com.deepdrilling.blockentities.sludgepump;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import com.deepdrilling.blockentities.module.ModuleBE;
import com.deepdrilling.fluid.Fluids;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public class SludgePumpModuleBE extends ModuleBE {
    public SludgePumpModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    public SludgePumpTank tank = new SludgePumpTank(1000);

    @Override
    public Set<ResourceLocation> getMutuallyExclusiveNames() {
        return Set.of(DrillMod.id("sludge_pump"));
    }

    private boolean canRun() {
        return tank.getMbAmount() < 1000;
    }
    private static final Modifier<Boolean, SludgePumpModuleBE> MODIFIER_FUNCTION = ModifierTypes.CAN_FUNCTION.create(
            ((core, head, be, base, prev) -> prev && be.canRun()), 0
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> (base + prev) / 8), -100
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            ((core, head, be, base, prev) -> prev * 0.5), -100
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_FORTUNE = ModifierTypes.FORTUNE.create(
            ((core, head, be, base, prev) -> prev * 0.55), -100
    );
    private static final Modifier<DrillHeadStats.WeightMultipliers, SludgePumpModuleBE> MODIFIER_WEIGHTS = ModifierTypes.RESOURCE_WEIGHT.create(
            ((core, head, be, base, prev) -> prev.mul(new DrillHeadStats.WeightMultipliers(1.5, 0.2, 0.2))), -100
    );
    private static final List<Modifier> MODIFIERS = List.of(MODIFIER_FUNCTION, MODIFIER_SPEED, MODIFIER_DAMAGE, MODIFIER_FORTUNE, MODIFIER_WEIGHTS);
    @Override
    public List<Modifier> getModifiers() {
        return MODIFIERS;
    }

    @Override
    public void blockBroken(DrillCoreBE drill) {
        tank.forceInsert(Fluids.SLUDGE, drill.getLevel().random.nextInt(100, 200));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean  bl = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (!canRun()) {
            tooltip.add(Component.translatable("deepdrilling.goggle.sludge_pump.backed_up").withStyle(ChatFormatting.RED));
            return true;
        }
        return bl;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        tank.read(compound.getCompound("Tank"));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Tank", tank.write());
    }
}
