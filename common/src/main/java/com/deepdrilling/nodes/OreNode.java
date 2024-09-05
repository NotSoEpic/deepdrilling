package com.deepdrilling.nodes;

import com.deepdrilling.DrillHeadStats.WeightMultipliers;
import com.deepdrilling.DrillMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class OreNode {
    public static OreNode EMPTY = new OreNode(DrillMod.id("empty"), "", "", "", WeightMultipliers.ZERO);
    ResourceLocation name;
    String earthTable;
    String commonTable;
    String rareTable;
    public WeightMultipliers weights;

    public OreNode(ResourceLocation name, String earth, String common, String rare, WeightMultipliers weights) {
        this.name = name;
        this.earthTable = earth;
        this.commonTable = common;
        this.rareTable = rare;
        this.weights = weights;
    }

    public OreNode(ResourceLocation name, OreNodeFormat.DrillLoots format) {
        this(name, format.earth, format.common, format.rare, new WeightMultipliers(format.getEarthMod(), format.getCommonMod(), format.getRareMod()));
    }

    public String getTableName(LOOT_TYPE type) {
        return switch (type) {
            case EARTH -> earthTable;
            case COMMON -> commonTable;
            case RARE -> rareTable;
            default -> "";
        };
    }

    @Nullable
    public LootTable getTable(ServerLevel level, LOOT_TYPE type) {
        return level.getServer().getLootData().getLootTable(new ResourceLocation(getTableName(type)));
    }

    public boolean hasTable(ServerLevel level, LOOT_TYPE type) {
        return getTable(level, type) != LootTable.EMPTY;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public boolean hasTables() {
        return (!Objects.equals(earthTable, "") || !Objects.equals(commonTable, "") || !Objects.equals(rareTable, ""))
                && !weights.isZero();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() == ResourceLocation.class) return name.equals(o);
        if (o == null || getClass() != o.getClass()) return false;
        OreNode oreNode = (OreNode) o;
        return Objects.equals(name, oreNode.name);
    }

    public enum LOOT_TYPE {
        EARTH, COMMON, RARE, NONE
    }
}
