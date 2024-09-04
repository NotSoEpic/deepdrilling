package com.deepdrilling.worldgen.fabric;

import com.deepdrilling.DrillMod;
import com.deepdrilling.worldgen.OreNodePiece;
import com.deepdrilling.worldgen.OreNodeStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OreNodeStructureImpl {
    private static final StructureType<OreNodeStructure> STRUCTURE_TYPE =
            Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DrillMod.id("ore_node"), () -> OreNodeStructure.CODEC);
    private static final StructurePieceType PIECE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, DrillMod.id("ore_node"), OreNodePiece::new);
    public static void init() {
    }

    public static StructureType<OreNodeStructure> getStructureType() {
        return STRUCTURE_TYPE;
    }

    public static StructurePieceType getPieceType() {
        return PIECE_TYPE;
    }
}
