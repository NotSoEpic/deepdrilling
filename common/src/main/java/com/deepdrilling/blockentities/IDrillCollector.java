package com.deepdrilling.blockentities;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public interface IDrillCollector extends IModule {
    // left - right -> sort in descending
    // right - left -> sort in ascending
    Comparator<Tuple<Integer, IDrillCollector>> drillCollectComparator = (lhs, rhs) -> {
        if (lhs.getB().drillCollectPriority() == rhs.getB().drillCollectPriority()) {
            // in tiebreaker, sort distance by ascending
            return lhs.getA() - rhs.getA();
        }
        // sort priority by descending
        return lhs.getB().drillCollectPriority() - rhs.getB().drillCollectPriority();
    };
    default int drillCollectPriority() {
        return 0;
    }
    List<ItemStack> collectItems(List<ItemStack> items);
}
