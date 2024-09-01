package com.deepdrilling.blockentities;

import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

public class SludgePumpModuleBE extends ModuleBE {
    public SludgePumpModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public Set<ResourceLocation> getMutuallyExclusiveNames() {
        return Set.of(DrillMod.id("sludge_pump"));
    }

    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> (base + prev) / 8), -100
    );
    private static final Modifier<Double, SludgePumpModuleBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            (((core, head, be, base, prev) -> prev * 0.5)), -100
    );
    private static final List<Modifier> MODIFIERS = List.of(MODIFIER_SPEED, MODIFIER_DAMAGE);

    @Override
    public List<Modifier> getModifiers() {
        return MODIFIERS;
    }
}
