package com.deepdrilling.blocks;

import com.deepdrilling.DrillHeadStats;

public interface IModuleTooltip {
    default boolean unique() { return false; }

    // -1 for beneficial, +1 for detrimental
    default int speedModifier() { return 0; }

    // -1 for beneficial, +1 for detrimental
    default int damageModifier() { return 0; }

    default DrillHeadStats.WeightMultipliers getWeightMultipliers() { return DrillHeadStats.WeightMultipliers.ONE; }
}
