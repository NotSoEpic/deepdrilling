package com.deepdrilling.blockentities;

import net.minecraft.util.Tuple;

import java.util.Comparator;

public interface IDrillSpeedMod extends IModule {
    // left - right -> sort in ascending
    // right - left -> sort in descending
    Comparator<Tuple<Integer, IDrillSpeedMod>> drillCollectComparator = (lhs, rhs) -> {
        if (lhs.getB().speedModPriority() == rhs.getB().speedModPriority()) {
            // in tiebreaker, sort distance by ascending
            return rhs.getA() - lhs.getA();
        }
        // sort priority by descending
        return rhs.getB().speedModPriority() - lhs.getB().speedModPriority();
    };
    default int speedModPriority() {
        return 0;
    }

    double modifySpeed(double base, double old);
}
