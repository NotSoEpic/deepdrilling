package com.deepdrilling.fabric.worldgen;

import com.deepdrilling.worldgen.OreNodes;
import net.minecraft.core.Registry;

public class OreNodeManager {

    public static void init() {
        Registry.register(Registry.FEATURE, OreNodes.ORE_NODE_ID, OreNodes.ORE_NODE);
    }
}
