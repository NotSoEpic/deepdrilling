package com.deepdrilling.forge;

import com.deepdrilling.ModuleStatTooltips;
import com.deepdrilling.blocks.IModuleTooltip;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class ModuleStatTooltipsForge implements TooltipModifier {
    IModuleTooltip moduleTooltip;
    public ModuleStatTooltipsForge(IModuleTooltip moduleTooltip) {
        this.moduleTooltip = moduleTooltip;
    }
    @Override
    public void modify(ItemTooltipEvent itemTooltipEvent) {
        List<Component> stats = ModuleStatTooltips.getModuleStats(moduleTooltip, itemTooltipEvent.getEntity());
        if (!stats.isEmpty()) {
            itemTooltipEvent.getToolTip().addAll(stats);
        }
    }

    public static ModuleStatTooltipsForge create(Item item) {
        if (item instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof IModuleTooltip moduleBlock) {
                return new ModuleStatTooltipsForge(moduleBlock);
            }
        }
        return null;
    }
}
