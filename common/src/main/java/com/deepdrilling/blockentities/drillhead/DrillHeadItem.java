package com.deepdrilling.blockentities.drillhead;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

public class DrillHeadItem extends BlockItem {@Unique
    public static final List<Enchantment> enchantments = new ArrayList<>(List.of(
        Enchantments.UNBREAKING, Enchantments.BLOCK_EFFICIENCY, Enchantments.BLOCK_FORTUNE));

    public DrillHeadItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return super.getBarWidth(stack);
    }
}
