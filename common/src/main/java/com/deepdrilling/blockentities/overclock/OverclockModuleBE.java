package com.deepdrilling.blockentities.overclock;

import com.deepdrilling.blockentities.ModuleBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class OverclockModuleBE extends ModuleBE {
    public OverclockModuleBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    private static final Modifier<Double, OverclockModuleBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> prev * 0.75f - base * 0.05f), 0
    );
    private static final Modifier<Double, OverclockModuleBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            (((core, head, be, base, prev) -> prev + 1)), 1
    );
    private static final List<Modifier> MODIFIERS = List.of(MODIFIER_SPEED, MODIFIER_DAMAGE);

    @Override
    public List<Modifier> getModifiers() {
        return MODIFIERS;
    }
}
