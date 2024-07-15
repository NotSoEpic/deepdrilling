package com.deepdrilling.fabric;

import com.deepdrilling.blocks.DrillHeadBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.world.level.block.Block;

public class BlockstateHelperImpl {
    public static <T extends Block> void existingFile(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, String location) {
        p.directionalBlock(c.get(), p.models().getExistingFile(p.modLoc(location)));
    }

    public static void makeDrillModelThatDoesWeirdStuff(DataGenContext<Block, DrillHeadBlock> ctx,
                                                     RegistrateBlockstateProvider prov,
                                                     String blockID, String headTexture) {
        // empty model file with just particles, for the block rendering
        prov.getVariantBuilder(ctx.get()).forAllStates(
                state -> ConfiguredModel.builder()
                        .modelFile(prov.models().getBuilder("block/" + blockID + "_block")
                                .texture("particle", headTexture)).build()
        );
        // model file with actuall stuff, for the partial model
        ConfiguredModel.builder()
                .modelFile(prov.models()
                        .withExistingParent(blockID, prov.modLoc("block/base_drill_head"))
                        .texture("0", headTexture));
    }
}
