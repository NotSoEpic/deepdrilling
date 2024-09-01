package com.deepdrilling.forge.mixin;

import com.deepdrilling.blockentities.drillhead.DrillHeadItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrillHeadItem.class)
public abstract class DrillHeadItemMixin implements IForgeItem {
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return DrillHeadItem.enchantments.contains(enchantment);
    }
}
