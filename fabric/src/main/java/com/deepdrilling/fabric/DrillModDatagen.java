package com.deepdrilling.fabric;

import com.deepdrilling.DrillMod;
import com.deepdrilling.fabric.datagen.DrillSequencedRecipes;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Paths;
import java.util.Set;
import java.util.function.BiConsumer;

public class DrillModDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator datagen) {
        DrillMod.LOGGER.info("Starting datagen!");
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES))), Set.of("create"), false, null, null);

        FabricDataGenerator.Pack pack = datagen.createPack();
        DrillMod.REGISTRATE.setupDatagen(pack, helper);

        DrillMod.REGISTRATE.addLang("itemGroup", DrillMod.id("main"), "Deep Drilling");

        DrillMod.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            PonderLocalization.generateSceneLang();
            PonderLocalization.provideLang(DrillMod.MOD_ID, langConsumer);
            langConsumer.accept("create.deepdrilling.recipe.ore_node", "Ore Node Drilling");
            langConsumer.accept("deepdrilling.loot.earth", "Earth");
            langConsumer.accept("deepdrilling.loot.common", "Common");
            langConsumer.accept("deepdrilling.loot.rare", "Rare");

            langConsumer.accept("deepdrilling.goggle.sludge_pump.backed_up", "Backed up");
        });


        pack.addProvider(DrillSequencedRecipes::new);
        // different fluid constants between forge/fabric fuck everything up
//        pack.addProvider(MixingRecipes::new);

        /*
        TODO datagen maybe idk
         - basic crafting recipes lol
         - ore node + loot table (!!)
         - configured + placed features
         - biome tags
         */
    }
}
