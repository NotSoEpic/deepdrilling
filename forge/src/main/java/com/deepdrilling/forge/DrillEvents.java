package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.OreNodes;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber
public class DrillEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new DrillTableProvider(event.getGenerator())
        );
    }

    @SubscribeEvent
    public static void reloadListener(AddReloadListenerEvent event) {
        DrillMod.LOGGER.info("registering reload listener");
        event.addListener((preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
            OreNodes.apply(resourceManager);

            CompletableFuture<Void> future = new CompletableFuture<>();
            future.complete(null);
            return future;
        });
    }
}
