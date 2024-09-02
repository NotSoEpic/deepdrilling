package com.deepdrilling.blockentities.module;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;

import java.util.List;

public class ModifierTypes {
    // !!! should always return (prev && [condition])
    public static Modifier.Type<Boolean>
            // true -> whether the drill can actually function
            CAN_FUNCTION = new Modifier.Type<>(Boolean.class, DrillMod.id("can_function"));

    public static Modifier.Type<Double>
            // base speed, determined by RPM -> ticks to mine
            SPEED = new Modifier.Type<>(Double.class, DrillMod.id("speed")),
            // 1 -> damage applied to drill
            DAMAGE = new Modifier.Type<>(Double.class, DrillMod.id("damage")),
            // 1 -> times to roll loot tables (fractional has a chance)
            FORTUNE = new Modifier.Type<>(Double.class, DrillMod.id("fortune"));

    public static Modifier.Type<DrillHeadStats.WeightMultipliers>
            // (1, 1, 1) -> weight of each resource category
            RESOURCE_WEIGHT = new Modifier.Type<>(DrillHeadStats.WeightMultipliers.class, DrillMod.id("resource_weight"));

    // should be List<ItemStack> but whatever, thanks type erasure
    public static Modifier.Type<List>
            // list of mined items -> excess to drop onto ground
            OUTPUT_LIST = new Modifier.Type<>(List.class, DrillMod.id("output_list"));
}
