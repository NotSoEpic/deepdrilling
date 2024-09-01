package com.deepdrilling.fabric.mixin;

import com.deepdrilling.blockentities.drillhead.DrillHeadItem;
import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrillHeadItem.class)
public abstract class DrillHeadItemMixin implements CustomEnchantingBehaviorItem {
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return DrillHeadItem.enchantments.contains(enchantment);
    }
}
