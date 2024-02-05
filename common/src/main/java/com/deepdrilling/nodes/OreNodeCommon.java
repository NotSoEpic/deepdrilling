package com.deepdrilling.nodes;

import com.deepdrilling.DrillMod;
import net.minecraft.resources.ResourceLocation;

public class OreNodeCommon {
    public static final ResourceLocation ORE_NODE_ID = DrillMod.id("ore_node");
    public static final OreNodeFeature ORE_NODE = new OreNodeFeature(OreNodeConfiguration.CODEC);
}
