package com.deepdrilling.fabric;

import com.deepdrilling.DrillHeadTooltips;
import com.deepdrilling.DrillMod;
import com.deepdrilling.fluid.fabric.FluidsImpl;
import com.deepdrilling.nodes.LootParser;
import com.deepdrilling.nodes.OreNodes;
import com.deepdrilling.worldgen.fabric.OreNodeStructureImpl;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class DrillModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DrillMod.REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                .andThen(TooltipModifier.mapNull(DrillHeadTooltips.create(item)))
        );

//        DrillMod.BASE_CREATIVE_TAB = new DrillCreativeTab(ItemGroupUtil.expandArrayAndGetId(), "deepdrilling.creative_tab");
        DrillMod.init();
        FluidsImpl.init();
        OreNodeStructureImpl.init();
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

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(((server, resourceManager) -> {
            LootParser.invalidate();
        }));

        // loaded in sendToPlayer()
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(((server, resourceManager, success) -> {
//            LootParser.parseOreNodes(server.getLootData(), OreNodes.getNodeMap());
//        }));

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            LootParser.sendToPlayer(player);
        }));
    }
}
