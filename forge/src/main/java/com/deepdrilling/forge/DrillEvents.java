package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.LootParser;
import com.deepdrilling.nodes.NodeReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber
public class DrillEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void reloadListener(AddReloadListenerEvent event) {
        DrillMod.LOGGER.info("Registering reload listener");
        event.addListener(new NodeReloadListener());
        LootParser.invalidate();
    }

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        DrillModClientForge.init();
    }

    @SubscribeEvent
    public static void syncToPlayer(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            LootParser.sendToPlayer(event.getPlayer());
        } else {
            event.getPlayerList().getPlayers().forEach(LootParser::sendToPlayer);
        }
    }
}
