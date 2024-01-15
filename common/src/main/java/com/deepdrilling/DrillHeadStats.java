package com.deepdrilling;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;

public class DrillHeadStats {
    public static final Map<ResourceLocation, Double> DRILL_DURABILITY = new HashMap<>();
    public static final Map<ResourceLocation, Double> DRILL_SPEED_MODIFIERS = new HashMap<>();

    public static <B extends Block, P>NonNullUnaryOperator<BlockBuilder<B, P>> setDurability(double value) {
        return b -> {
            DRILL_DURABILITY.put(
                    new ResourceLocation(b.getOwner().getModid(), b.getName()),
                    value
            );
            return b;
        };
    }
    public static <B extends Block, P>NonNullUnaryOperator<BlockBuilder<B, P>> setSpeedModifier(double value) {
        return b -> {
            DRILL_SPEED_MODIFIERS.put(
                    new ResourceLocation(b.getOwner().getModid(), b.getName()),
                    value
            );
            return b;
        };
    }
}
