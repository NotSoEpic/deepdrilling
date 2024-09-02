package com.deepdrilling;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;

public class DItems {
    public static ItemEntry<SequencedAssemblyItem> INCOMPLETE_COPPER_DRILL_HEAD = DrillMod.REGISTRATE
            .item("incomplete_copper_drill_head", SequencedAssemblyItem::new)
            .model((c, p) -> {
                p.withExistingParent(c.getName(), p.modLoc("item/base_incomplete_drill_head"))
                        .texture("0", "minecraft:block/copper_block");
            })
            .register(),
    INCOMPLETE_BRASS_DRILL_HEAD = DrillMod.REGISTRATE
            .item("incomplete_brass_drill_head", SequencedAssemblyItem::new)
            .model((c, p) -> {
                p.withExistingParent(c.getName(), p.modLoc("item/base_incomplete_drill_head"))
                        .texture("0", "create:block/brass_block");
            })
            .register();

    public static void init() {

    }
}
