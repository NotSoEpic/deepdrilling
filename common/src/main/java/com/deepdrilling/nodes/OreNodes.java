package com.deepdrilling.nodes;

import com.deepdrilling.DrillMod;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class OreNodes {
    private static Map<Block, OreNode> nodeMap = ImmutableMap.of();


    public static OreNode get(Block block) {
        return nodeMap.getOrDefault(block, OreNode.EMPTY);
    }

    public static LootTable get(Block block, OreNode.LOOT_TYPE type, ServerLevel level) {
        return get(block).getTable(level, type);
    }

    public static Map<Block, OreNode> getNodeMap() {
        return nodeMap;
    }

    public static HashMap<ResourceLocation, OreNodeFormat> prepare(ResourceManager manager) {
        HashMap<ResourceLocation, OreNodeFormat> nodes = new HashMap<>();
        manager.listResources("ore_nodes", resourceLocation -> true).forEach(
                ((resourceLocation, resource) -> {
                    try {
                        Reader reader = new InputStreamReader(resource.open());
                        OreNodeFormat json = new Gson().fromJson(reader, OreNodeFormat.class);
                        nodes.put(resourceLocation, json);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        return nodes;
    }

    public static void apply(HashMap<ResourceLocation, OreNodeFormat> nodeList) {
        ImmutableMap.Builder<Block, OreNode> builder = ImmutableMap.builder();
        for (Map.Entry<ResourceLocation, OreNodeFormat> entry : nodeList.entrySet()) {
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(entry.getValue().block));
            if (block != Blocks.AIR) {
                OreNode node = new OreNode(entry.getKey(), entry.getValue().loot_tables);
                builder.put(block, node);
            } else {
                DrillMod.LOGGER.error("Could not find block " + entry.getValue().block);
            }
        }
        nodeMap = builder.build();
    }

    public static void doBoth(ResourceManager manager) {
        apply(prepare(manager));
    }
}
