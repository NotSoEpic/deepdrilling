package com.deepdrilling.fabric;

import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.OreNodes;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class DrillModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DrillMod.init();
        DrillMod.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), DrillMod.NAME);
        // on fabric, Registrates must be explicitly finalized and registered.
        DrillMod.REGISTRATE.register();
        registerReloadListeners();
    }

    private void registerReloadListeners() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return DrillMod.id("ore_nodes");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                OreNodes.doBoth(manager);
            }
        });
    }
}
