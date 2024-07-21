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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OreNodes {
    private static Map<Block, OreNode> nodes = ImmutableMap.of();

    public static OreNode get(Block block) {
        return nodes.getOrDefault(block, OreNode.EMPTY);
    }

    public static LootTable get(Block block, OreNode.LOOT_TYPE type, ServerLevel level) {
        return get(block).getTable(level, type);
    }

    public static Map<Block, OreNode> getNodes() {
        return nodes;
    }

    public static List<OreNodeFormat> prepare(ResourceManager manager) {
        ArrayList<OreNodeFormat> nodes = new ArrayList<>();
        manager.listResources("ore_nodes", resourceLocation -> true).forEach(
                ((resourceLocation, resource) -> {
                    try {
                        Reader reader = new InputStreamReader(resource.open());
                        OreNodeFormat json = new Gson().fromJson(reader, OreNodeFormat.class);
                        nodes.add(json);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        return nodes;
    }

    public static void apply(List<OreNodeFormat> nodeList) {
        ImmutableMap.Builder<Block, OreNode> builder = ImmutableMap.builder();
        for (OreNodeFormat node : nodeList) {
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(node.block));
            if (block != Blocks.AIR) {
                builder.put(block, new OreNode(node.loot_tables));
            } else {
                DrillMod.LOGGER.error("Could not find block " + node.block);
            }
        }
        nodes = builder.build();
    }

    public static void doBoth(ResourceManager manager) {
        apply(prepare(manager));
    }
}
