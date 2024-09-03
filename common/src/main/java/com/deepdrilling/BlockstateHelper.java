package com.deepdrilling;

import com.deepdrilling.blocks.DrillHeadBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;

public class BlockstateHelper {
    @ExpectPlatform
    public static <T extends Block> void existingFilePillar(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
    }

    @ExpectPlatform
    public static <T extends Block> void existingFileFacing(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
    }

    @ExpectPlatform
    public static void makeDrillModelThatDoesWeirdStuff(DataGenContext<Block, DrillHeadBlock> ctx,
                                                        RegistrateBlockstateProvider prov,
                                                        String blockID, String headTexture) {
    }
}
