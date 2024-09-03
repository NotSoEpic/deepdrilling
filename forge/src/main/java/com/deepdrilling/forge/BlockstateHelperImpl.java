package com.deepdrilling.forge;

import com.deepdrilling.blocks.DrillHeadBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;

public class BlockstateHelperImpl {
    public static <T extends Block> void existingFilePillar(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
    }

    public static <T extends Block> void existingFileFacing(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
    }

    public static void makeDrillModelThatDoesWeirdStuff(DataGenContext<Block, DrillHeadBlock> ctx, RegistrateBlockstateProvider prov, String blockID, String headTexture) {
    }
}
