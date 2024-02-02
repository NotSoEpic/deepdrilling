package com.deepdrilling.forge;

import com.deepdrilling.DrillHeadTooltips;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class DrillHeadTooltipsForge implements TooltipModifier {
    @Override
    public void modify(ItemTooltipEvent itemTooltipEvent) {
        List<Component> stats = DrillHeadTooltips.getDrillStats(Registry.ITEM.getKey(itemTooltipEvent.getItemStack().getItem()), itemTooltipEvent.getEntity());
        if (!stats.isEmpty()) {
            itemTooltipEvent.getToolTip().addAll(stats);
        }
    }

    public static DrillHeadTooltipsForge create(Item item) {
        if (DDrillHeads.knownDrillHeads.stream().anyMatch(entry -> entry.get().asItem() == item)) {
            return new DrillHeadTooltipsForge();
        }
        return null;
    }
}
