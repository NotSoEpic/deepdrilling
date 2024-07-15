package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.BlockstateHelper;
import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class DDrillHeads {
    public static final List<BlockEntry<DrillHeadBlock>> knownDrillHeads = new ArrayList<>();
    public static final Map<ResourceLocation, PartialModel> partialModels = new HashMap<>();

    // only this stuff should need to be touched
    public static final BlockEntry<DrillHeadBlock> ANDESITE, BRASS, COPPER;
    static {
        ANDESITE = createDrillHead("andesite_drill_head", "minecraft:block/andesite", "Andesite Drill Head",
                100, 4, 1, 1.5, 1, 0.5);
        BRASS = createDrillHead("brass_drill_head", "create:block/brass_block", "Brass Drill Head",
                500, 8, 0.8, 0, 1.5, 1.5);
        COPPER = createDrillHead("copper_drill_head", "minecraft:block/copper_block", "Copper Drill Head",
                200, 4, 0.5, 2, 0, 0);
    }

    // cursed java below this line to make the stuff above the line all nice and clean looking :3
    // nvm the stuff above is beginning to look a little less clean 3:
    public static void createCopyHeadDataDrop(RegistrateBlockLootTables tables, Block block) {
        tables.add(block, LootTable.lootTable().withPool(
                (tables.applyExplosionCondition(block, LootPool.lootPool())).setRolls(ConstantValue.exactly(1)).add(
                        LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Damage", "Damage")
                        )))
        );
    }

    public static BlockEntityEntry<DrillHeadBE> DRILL_HEAD_BE;
    public static BlockEntry<DrillHeadBlock> createDrillHead(String blockID, String headTexture, String name,
                                                             double durability, double stress, double miningTimeModifier,
                                                             double earthWeight, double commonWeight, double rareWeight) {
        BlockEntry<DrillHeadBlock> block = DrillMod.REGISTRATE
                .block(blockID, DrillHeadBlock::new)
                .addLayer(() -> RenderType::cutout)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(pickaxeOnly())
                .transform(BlockStressDefaults.setImpact(stress))
                .transform(DrillHeadStats.setDurability(durability))
                .transform(DrillHeadStats.setSpeedModifier(miningTimeModifier))
                .transform(DrillHeadStats.setLootWeightMultiplier(earthWeight, commonWeight, rareWeight))
                .loot(DDrillHeads::createCopyHeadDataDrop)
                .blockstate((c, p) -> BlockstateHelper.makeDrillModelThatDoesWeirdStuff(c, p, blockID, headTexture))
                .lang(name)
                .item(DrillHeadItem::new)
                .properties(p -> p.durability((int) durability))
                .build()
                .register();
        knownDrillHeads.add(block);
        getPartialModel(block.getId());
        return block;
    }

    public static void registerBlockEntity() {
        CreateBlockEntityBuilder<DrillHeadBE, CreateRegistrate> builder = DrillMod.REGISTRATE
                .blockEntity("drill_head", DrillHeadBE::new)
                .instance(() -> DrillHeadInstance::new, false);

        for (BlockEntry<DrillHeadBlock> drillHeadBlock : knownDrillHeads) {
            builder.validBlocks(drillHeadBlock);
        }

        DRILL_HEAD_BE = builder.renderer(() -> DrillHeadRenderer::new)
                .register();
    }

    public static PartialModel getPartialModel(ResourceLocation blockID) {
        if (!partialModels.containsKey(blockID)) {
            partialModels.put(blockID, new PartialModel(
                    new ResourceLocation(blockID.getNamespace(), "block/" + blockID.getPath())
            ));
        }
        return partialModels.get(blockID);
    }
    public static PartialModel getPartialModel(BlockState state) {
        return getPartialModel(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
    }

    public static void init() {}
}
