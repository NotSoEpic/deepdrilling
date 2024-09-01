package com.deepdrilling.blockentities.module;

import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

/**
 * A block entity that modifies the drills behaviour
 */
public interface Module {
    /**
     * @return name displayed on a Drill Core's module list tooltip
     */
    MutableComponent getName();

    /**
     * @return list of modifiers that are applied to the drill
     */
    List<Modifier> getModifiers();


    default void progressBreaking(DrillCoreBE drill) {}
    default void blockBroken(DrillCoreBE drill) {}
}
