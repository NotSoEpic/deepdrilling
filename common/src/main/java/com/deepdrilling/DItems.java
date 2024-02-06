package com.deepdrilling;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class DItems {
    public static ItemEntry<SequencedAssemblyItem> INCOMPLETE_COPPER_DRILL_HEAD = DrillMod.REGISTRATE
            .item("incomplete_copper_drill_head", SequencedAssemblyItem::new)
            .register(),
    INCOMPLETE_BRASS_DRILL_HEAD = DrillMod.REGISTRATE
            .item("incomplete_brass_drill_head", SequencedAssemblyItem::new)
            .register();
    public static void init() {

    }
}
