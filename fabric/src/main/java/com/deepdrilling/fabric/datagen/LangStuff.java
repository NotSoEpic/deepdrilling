package com.deepdrilling.fabric.datagen;

import com.deepdrilling.DBlocks;
import com.deepdrilling.DrillMod;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiConsumer;

public class LangStuff {
    private static BiConsumer<String, String> langConsumer;
    public static void register(RegistrateLangProvider provider) {
        langConsumer = provider::add;

        PonderLocalization.generateSceneLang();
        PonderLocalization.provideLang(DrillMod.MOD_ID, langConsumer);

        langConsumer.accept("create.deepdrilling.recipe.ore_node", "Ore Node Drilling");
        langConsumer.accept("deepdrilling.loot.earth", "Earth");
        langConsumer.accept("deepdrilling.loot.common", "Common");
        langConsumer.accept("deepdrilling.loot.rare", "Rare");

        langConsumer.accept("deepdrilling.goggle.sludge_pump.backed_up", "Backed up");

        summary(DBlocks.ASURINE_NODE, "Found _Bedrock_ in _Cold Biomes_");
        conditionBehaviour(DBlocks.ASURINE_NODE, 1, "Surface deposit", "_Asurine, Tuff, Calcite_");
        summary(DBlocks.CRIMSITE_NODE, "Found _Bedrock_ in _Mountain Biomes_");
        conditionBehaviour(DBlocks.CRIMSITE_NODE, 1, "Surface deposit", "_Crimsite, Tuff, Limestone_");
        summary(DBlocks.OCHRUM_NODE, "Found _Bedrock_ in _Desert Biomes_");
        conditionBehaviour(DBlocks.OCHRUM_NODE, 1, "Surface deposit", "_Ochrum, Tuff, Dripstone_");
        summary(DBlocks.VERIDIUM_NODE, "Found _Bedrock_ in _Ocean Biomes_");
        conditionBehaviour(DBlocks.VERIDIUM_NODE, 1, "Surface deposit", "_Veridium, Tuff, Andesite_");
    }

    private static void summary(NonNullSupplier<? extends ItemLike> item, String summary) {
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.summary", summary);
    }

    private static void conditionBehaviour(NonNullSupplier<? extends ItemLike> item, int i, String condition, String behaviour) {
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.condition" + i, condition);
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.behaviour" + i, behaviour);
    }
}
