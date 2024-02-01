package com.deepdrilling.nodes;

import com.deepdrilling.DrillMod;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class OreNodes {
    private static final Map<Block, OreNode> nodes = ImmutableMap.of();

    public static void apply(ResourceManager manager) {
        DrillMod.LOGGER.info("Beginning resource reload!");
        manager.listResources("ore_nodes", resourceLocation -> true).forEach(
                ((resourceLocation, resource) -> {
                    try {
                        Reader reader = new InputStreamReader(resource.open());
                        JsonElement json = new Gson().fromJson(reader, JsonElement.class);
                        DrillMod.LOGGER.info(resourceLocation.toString());
                        DrillMod.LOGGER.info(json.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }
}
