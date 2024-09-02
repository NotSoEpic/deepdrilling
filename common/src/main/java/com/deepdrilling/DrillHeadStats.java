package com.deepdrilling;

import com.deepdrilling.nodes.OreNode;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrillHeadStats {
    public static final Map<ResourceLocation, Double> DRILL_DURABILITY = new HashMap<>();
    public static final Map<ResourceLocation, Double> DRILL_SPEED_MODIFIERS = new HashMap<>();
    public static final Map<ResourceLocation, WeightMultipliers> LOOT_WEIGHT_MULTIPLIER = new HashMap<>();

    public static double getDrillDurability(ResourceLocation drill) {
        return DRILL_DURABILITY.getOrDefault(drill, 100.0);
    }
    public static double getDrillSpeedModifier(ResourceLocation drill) {
        return DRILL_SPEED_MODIFIERS.getOrDefault(drill, 1.0);
    }
    public static WeightMultipliers getLootWeightMultiplier(ResourceLocation drill) {
        return LOOT_WEIGHT_MULTIPLIER.getOrDefault(drill, WeightMultipliers.ONE);
    }

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

    public static <B extends Block, P>NonNullUnaryOperator<BlockBuilder<B, P>> setLootWeightMultiplier(double earth, double common, double rare) {
        return b -> {
            LOOT_WEIGHT_MULTIPLIER.put(
                    new ResourceLocation(b.getOwner().getModid(), b.getName()),
                    new WeightMultipliers(earth, common, rare)
            );
            return b;
        };
    }

    public static class WeightMultipliers {
        public static WeightMultipliers ONE = new WeightMultipliers(1, 1, 1);
        public static WeightMultipliers ZERO = new WeightMultipliers(0, 0, 0);
        public double earth;
        public double common;
        public double rare;
        public WeightMultipliers(double earth, double common, double rare) {
            this.earth = earth;
            this.common = common;
            this.rare = rare;
        }

        public boolean isZero() {
            return earth <= 0 && common <= 0 && rare <= 0;
        }
        public boolean isOne() {
            return earth == 1 && common == 1 && rare == 1;
        }

        public WeightMultipliers mul(WeightMultipliers other) {
            return new WeightMultipliers(
                    this.earth * other.earth,
                    this.common * other.common,
                    this.rare * other.rare
            );
        }

        public OreNode.LOOT_TYPE pick(RandomSource random) {
            if (earth <= 0 && common <= 0 && rare <= 0) {
                return OreNode.LOOT_TYPE.NONE;
            }
            double total = Math.max(earth, 0) + Math.max(common, 0) + Math.max(rare, 0);
            double rareThresh = (earth + common) / total;
            double commonThresh = earth / total;
            double choice = random.nextDouble();
            if (choice > rareThresh)
                return OreNode.LOOT_TYPE.RARE;
            if (choice > commonThresh)
                return OreNode.LOOT_TYPE.COMMON;
            return OreNode.LOOT_TYPE.EARTH;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj instanceof WeightMultipliers other) {
                return this.earth == other.earth && this.common == other.common && this.rare == other.rare;
            }
            return false;
        }

        public void addTooltip(List<Component> list, boolean hasGoggles, boolean showRedundant) {
            if (showRedundant || earth != 1)
                addCollectionModifier(list, earth, hasGoggles, "deepdrilling.loot.earth");
            if (showRedundant || common != 1)
                addCollectionModifier(list, common, hasGoggles, "deepdrilling.loot.common");
            if (showRedundant || rare != 1)
                addCollectionModifier(list, rare, hasGoggles, "deepdrilling.loot.rare");
        }

        private static void addCollectionModifier(List<Component> list, double mod, boolean goggles, String name) {
            Lang.builder().add(DrillHeadTooltips.makeProbabilityMultiplier(mod, goggles, name)).addTo(list);
        }
    }
}
