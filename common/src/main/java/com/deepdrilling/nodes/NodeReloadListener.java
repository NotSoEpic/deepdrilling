package com.deepdrilling.nodes;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;

public class NodeReloadListener extends SimplePreparableReloadListener<List<OreNodeFormat>> {
    @Override
    protected List<OreNodeFormat> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        return OreNodes.prepare(resourceManager);
    }

    @Override
    protected void apply(List<OreNodeFormat> nodes, ResourceManager resourceManager, ProfilerFiller profiler) {
        OreNodes.apply(nodes);
    }
}
