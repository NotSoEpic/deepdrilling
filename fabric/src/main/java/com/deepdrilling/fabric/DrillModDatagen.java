package com.deepdrilling.fabric;

import com.deepdrilling.DrillMod;
import com.deepdrilling.fabric.datagen.DrillSequencedRecipes;
import com.deepdrilling.fabric.datagen.LangStuff;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Paths;
import java.util.Set;

public class DrillModDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator datagen) {
        DrillMod.LOGGER.info("Starting datagen!");
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES))), Set.of("create"), false, null, null);

        FabricDataGenerator.Pack pack = datagen.createPack();
        DrillMod.REGISTRATE.setupDatagen(pack, helper);

        DrillMod.REGISTRATE.addLang("itemGroup", DrillMod.id("main"), "Deep Drilling");

        DrillMod.REGISTRATE.addDataGenerator(ProviderType.LANG, LangStuff::register);

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
