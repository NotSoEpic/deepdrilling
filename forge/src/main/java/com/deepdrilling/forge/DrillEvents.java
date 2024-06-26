package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.NodeReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DrillEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void reloadListener(AddReloadListenerEvent event) {
        DrillMod.LOGGER.info("Registering reload listener");
        event.addListener(new NodeReloadListener());
    }
}
