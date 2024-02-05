package com.deepdrilling;

import com.deepdrilling.blocks.*;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class DBlocks {
	/**
	 * for drill heads
	 * @see com.deepdrilling.blockentities.drillhead.DDrillHeads
	 */
	public static final BlockEntry<DrillCore> DRILL = DrillMod.REGISTRATE
			.block("drill_core", DrillCore::new)
			.addLayer(() -> RenderType::cutout)
			.initialProperties(SharedProperties::stone)
			.properties(p ->  p.color(MaterialColor.PODZOL))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.lang("Drill Core")
			.blockstate(BlockStateGen.directionalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(16))
			.item()
			//.properties(p -> p.tab(DrillMod.CREATIVE_TAB))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<CollectorModuleBlock> COLLECTOR = DrillMod.REGISTRATE
			.block("collection_filter", CollectorModuleBlock::new)
			.properties(p ->  p.color(MaterialColor.PODZOL))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.lang("Collection Filter")
			.transform(BlockStressDefaults.setImpact(2))
			.item()
			//.properties(p -> p.tab(DrillMod.CREATIVE_TAB))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<OverclockModuleBlock> DRILL_OVERCLOCK = DrillMod.REGISTRATE
			.block("drill_overclock", OverclockModuleBlock::new)
			.addLayer(() -> RenderType::cutout)
			.lang("Drill Overclock")
			.properties(p ->  p.color(MaterialColor.PODZOL))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(pickaxeOnly())
			.transform(BlockStressDefaults.setImpact(8))
			.item()
			//.properties(p -> p.tab(DrillMod.CREATIVE_TAB))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<SludgePumpModuleBlock> SLUDGE_PUMP = DrillMod.REGISTRATE
			.block("sludge_pump", SludgePumpModuleBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.transform(BlockStressDefaults.setImpact(4))
			.simpleItem()
			.register();

	public static final BlockEntry<OreNodeBlock> CRIMSITE_NODE = DrillMod.REGISTRATE
			.block("crimsite_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			.simpleItem()
			.register(),
	ASURINE_NODE = DrillMod.REGISTRATE
			.block("asurine_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			.simpleItem()
			.register(),
	OCHRUM_NODE = DrillMod.REGISTRATE
			.block("ochrum_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			.simpleItem()
			.register(),
	VERIDIUM_NODE = DrillMod.REGISTRATE
			.block("veridium_node", OreNodeBlock::new)
			.initialProperties(() -> Blocks.BEDROCK)
			.simpleItem()
			.register();

	public static void init() {
		DrillMod.LOGGER.info("Registering blocks for " + DrillMod.NAME);
	}
}
