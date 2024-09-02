package com.deepdrilling.nodes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;

public class NodeReloadListener extends SimplePreparableReloadListener<HashMap<ResourceLocation, OreNodeFormat>> {
    @Override
    protected HashMap<ResourceLocation, OreNodeFormat> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return OreNodes.prepare(resourceManager);
    }

    @Override
    protected void apply(HashMap<ResourceLocation, OreNodeFormat> nodes, ResourceManager resourceManager, ProfilerFiller profiler) {
        OreNodes.apply(nodes);
    }
}
