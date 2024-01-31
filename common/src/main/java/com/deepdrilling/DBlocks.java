package com.deepdrilling;

import com.deepdrilling.blocks.CollectorModuleBlock;
import com.deepdrilling.blocks.DrillCore;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.deepdrilling.blocks.OverclockModuleBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

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
			.item()
			//.properties(p -> p.tab(DrillMod.CREATIVE_TAB))
			.transform(ModelGen.customItemModel())
			.register();

	public static final BlockEntry<OverclockModuleBlock> DRILL_OVERCLOCK = DrillMod.REGISTRATE
			.block("drill_overclock", OverclockModuleBlock::new)
			.lang("Drill Overclock")
			.properties(p ->  p.color(MaterialColor.PODZOL))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.transform(axeOrPickaxe())
			.item()
			//.properties(p -> p.tab(DrillMod.CREATIVE_TAB))
			.transform(ModelGen.customItemModel())
			.register();

	public static void init() {
		DrillMod.LOGGER.info("Registering blocks for " + DrillMod.NAME);
	}
}
