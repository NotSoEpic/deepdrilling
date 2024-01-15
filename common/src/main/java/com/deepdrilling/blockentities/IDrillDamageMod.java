package com.deepdrilling.blockentities;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public interface IDrillDamageMod {
    // left - right -> sort in descending
    // right - left -> sort in ascending
    Comparator<Tuple<Integer, IDrillDamageMod>> drillDamageComparator = (lhs, rhs) -> {
        if (lhs.getB().drillDamagePriority() == rhs.getB().drillDamagePriority()) {
            // in tiebreaker, sort distance by ascending
            return lhs.getA() - rhs.getA();
        }
        // sort priority by descending
        return lhs.getB().drillDamagePriority() - rhs.getB().drillDamagePriority();
    };
    default int drillDamagePriority() {
        return 0;
    }
    double modifyDamage(double current);
}
