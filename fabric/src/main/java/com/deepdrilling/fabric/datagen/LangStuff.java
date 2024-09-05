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

        summary(DBlocks.BLANK_MODULE, "A _Module_ to be attached to a _Drill Core_.");
        conditionBehaviour(DBlocks.BLANK_MODULE, 1, "When attached to Drill Core", "Does _nothing_... but still _connects other Modules_.");
        summary(DBlocks.COLLECTOR, "A _Module_ to be attached to a _Drill Core_.");
        conditionBehaviour(DBlocks.COLLECTOR, 1, "When attached to Drill Core", "_Collects items_ that would otherwise be _dropped on the ground_.");
        conditionBehaviour(DBlocks.COLLECTOR, 2, "When R-Clicked", "_Gives collected items_ in its inventory.");
        summary(DBlocks.DRILL_OVERCLOCK, "A _Module_ to be attached to a _Drill Core_.");
        conditionBehaviour(DBlocks.DRILL_OVERCLOCK, 1, "When attached to Drill Core", "_Increases speed_, but also _increases damage taken_.");
        summary(DBlocks.SLUDGE_PUMP, "A _Module_ to be attached to a _Drill Core_. _Only one_ can be used.");
        conditionBehaviour(DBlocks.SLUDGE_PUMP, 1, "When attached to Drill Core", "_Increases speed_ and _reduces damage taken_, but also _reduces quality and quantity of items_ while accumulating _Sludge_.");
        conditionBehaviour(DBlocks.SLUDGE_PUMP, 2, "When full of Sludge", "Attached drill _stops working_.");

        summary(DBlocks.ASURINE_NODE, "Found at _Bedrock_ level in _Cold Biomes_");
        conditionBehaviour(DBlocks.ASURINE_NODE, 1, "Surface deposit", "_Asurine, Tuff, Calcite_");
        summary(DBlocks.CRIMSITE_NODE, "Found at _Bedrock_ level in _Mountain Biomes_");
        conditionBehaviour(DBlocks.CRIMSITE_NODE, 1, "Surface deposit", "_Crimsite, Tuff, Limestone_");
        summary(DBlocks.OCHRUM_NODE, "Found at _Bedrock_ level in _Desert Biomes_");
        conditionBehaviour(DBlocks.OCHRUM_NODE, 1, "Surface deposit", "_Ochrum, Tuff, Dripstone_");
        summary(DBlocks.VERIDIUM_NODE, "Found at _Bedrock_ level in _Ocean Biomes_");
        conditionBehaviour(DBlocks.VERIDIUM_NODE, 1, "Surface deposit", "_Veridium, Tuff, Andesite_");
    }

    // required for any of the other tooltip stuff to appear
    private static void summary(NonNullSupplier<? extends ItemLike> item, String summary) {
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.summary", summary);
    }

    private static void conditionBehaviour(NonNullSupplier<? extends ItemLike> item, int i, String condition, String behaviour) {
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.condition" + i, condition);
        langConsumer.accept(item.get().asItem().getDescriptionId() + ".tooltip.behaviour" + i, behaviour);
    }
}
