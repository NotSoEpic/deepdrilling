package com.deepdrilling.worldgen.forge;

import com.deepdrilling.DrillMod;
import com.deepdrilling.forge.DrillModForge;
import com.deepdrilling.worldgen.OreNodePiece;
import com.deepdrilling.worldgen.OreNodeStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OreNodeStructureImpl {
    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, DrillMod.MOD_ID);
    private static final RegistryObject<StructureType<OreNodeStructure>> STRUCTURE_TYPE =
            STRUCTURE_TYPE_REGISTRY.register("ore_node", () -> () -> OreNodeStructure.CODEC);

    private static final DeferredRegister<StructurePieceType> PIECE_TYPE_REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, DrillMod.MOD_ID);
    private static final RegistryObject<StructurePieceType> PIECE_TYPE =
            PIECE_TYPE_REGISTRY.register("ore_node", () -> OreNodePiece::new);

    public static void init() {
        STRUCTURE_TYPE_REGISTRY.register(DrillModForge.getBus());
        PIECE_TYPE_REGISTRY.register(DrillModForge.getBus());
    }

    public static StructureType<OreNodeStructure> getStructureType() {
        return STRUCTURE_TYPE.get();
    }

    public static StructurePieceType getPieceType() {
        return PIECE_TYPE.get();
    }
}
