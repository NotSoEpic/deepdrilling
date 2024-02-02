package com.deepdrilling;

import com.deepdrilling.blocks.IModuleTooltip;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ModuleStatTooltips implements TooltipModifier {
    IModuleTooltip moduleTooltip;
    public ModuleStatTooltips(IModuleTooltip moduleTooltip) {
        this.moduleTooltip = moduleTooltip;
    }

    @Override
    public void modify(ItemStack stack, Player player, TooltipFlag flags, List<Component> tooltip) {
        List<Component> stats = getModuleStats(moduleTooltip, player);
        if (!stats.isEmpty()) {
            tooltip.addAll(stats);
        }
    }

    public static ModuleStatTooltips create(Item item) {
        if (item instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof IModuleTooltip moduleTooltip) {
                return new ModuleStatTooltips(moduleTooltip);
            }
        }
        return null;
    }

    public static List<Component> getModuleStats(IModuleTooltip moduleTooltip, Player player) {
        boolean hasGoggles = GogglesItem.isWearingGoggles(player);
        List<Component> list = new ArrayList<>();

        uniqueTooltip(list, moduleTooltip, hasGoggles);
        damageTooltip(list, moduleTooltip, hasGoggles);
        speedTooltip(list, moduleTooltip, hasGoggles);
        weightTooltip(list, moduleTooltip, hasGoggles);
        if (!list.isEmpty()) {
            list.add(0, Lang.text("Module Effects:").style(ChatFormatting.GRAY).component());
        }

        return list;
    }

    public static void uniqueTooltip(List<Component> list, IModuleTooltip moduleTooltip, boolean hasGoggles) {
        if (moduleTooltip.unique()) {
            Lang.text(" Unique").style(ChatFormatting.AQUA).addTo(list);
        }
    }

    public static void damageTooltip(List<Component> list, IModuleTooltip moduleTooltip, boolean hasGoggles) {
        int damage = moduleTooltip.damageModifier();
        if (damage < 0) {
            Lang.text(" Reduces damage").style(ChatFormatting.GREEN).addTo(list);
        }
        if (damage > 0) {
            Lang.text(" Increases damage").style(ChatFormatting.RED).addTo(list);
        }
    }

    public static void speedTooltip(List<Component> list, IModuleTooltip moduleTooltip, boolean hasGoggles) {
        int speed = moduleTooltip.speedModifier();
        if (speed < 0) {
            Lang.text(" Speeds up").style(ChatFormatting.GREEN).addTo(list);
        }
        if (speed > 0) {
            Lang.text(" Slows down").style(ChatFormatting.RED).addTo(list);
        }
    }

    public static void weightTooltip(List<Component> list, IModuleTooltip moduleTooltip, boolean hasGoggles) {
        DrillHeadStats.WeightMultipliers weights = moduleTooltip.getWeightMultipliers();
        if (!weights.isOne()) {
            weights.addTooltip(list, hasGoggles, false);
        }
    }
}
