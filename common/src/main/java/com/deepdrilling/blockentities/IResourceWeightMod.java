package com.deepdrilling.blockentities;

import com.deepdrilling.DrillHeadStats;

public interface IResourceWeightMod {
    default DrillHeadStats.WeightMultipliers getWeightMultiplier() {
        return DrillHeadStats.WeightMultipliers.ONE;
    };
}
