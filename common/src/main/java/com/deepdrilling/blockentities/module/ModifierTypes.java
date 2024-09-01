package com.deepdrilling.blockentities.module;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;

import java.util.List;

public class ModifierTypes {
    public static Modifier.Type<Double>
            SPEED = new Modifier.Type<>(Double.class, DrillMod.id("speed")),
            DAMAGE = new Modifier.Type<>(Double.class, DrillMod.id("damage"));

    public static Modifier.Type<DrillHeadStats.WeightMultipliers>
            RESOURCE_WEIGHT = new Modifier.Type<>(DrillHeadStats.WeightMultipliers.class, DrillMod.id("resource_weight"));

    // should be List<ItemStack> but whatever, thanks type erasure
    public static Modifier.Type<List>
            OUTPUT_LIST = new Modifier.Type<>(List.class, DrillMod.id("output_list"));
}
