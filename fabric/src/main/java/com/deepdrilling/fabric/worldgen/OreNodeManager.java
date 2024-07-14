package com.deepdrilling.fabric.worldgen;

import com.deepdrilling.worldgen.OreNodes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class OreNodeManager {

    public static void init() {
        Registry.register(BuiltInRegistries.FEATURE, OreNodes.ORE_NODE_ID, OreNodes.ORE_NODE);
    }
}
