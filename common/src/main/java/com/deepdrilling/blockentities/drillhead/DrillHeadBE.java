package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrillHeadBE extends KineticBlockEntity {
    protected double damage = 0;
    public DrillHeadBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setDamage(double amount) {
        damage = amount;
        if (damage >= getMaxDamage()) {
            level.destroyBlock(getBlockPos(), false);
            return;
        }
        setChanged();
        sendData();
    }

    public void applyDamage(double amount) {
        setDamage(damage + amount);
    }

    public double getMaxDamage() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.DRILL_DURABILITY.getOrDefault(name, 100.0);
    }

    public double getSpeedModifier() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.DRILL_SPEED_MODIFIERS.getOrDefault(name, 1.0);
    }

    public DrillHeadStats.WeightMultipliers getWeightMultipliers() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.LOOT_WEIGHT_MULTIPLIER.getOrDefault(name, DrillHeadStats.WeightMultipliers.ONE);
    }

    public ItemStack setItemDamage(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        tag.putInt("Damage", (int)damage);
        return item;
    }

    public void applyItemDamage(ItemStack item) {
        setDamage(item.getOrCreateTag().getInt("Damage"));
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        Lang.text("Head Durability:").style(ChatFormatting.GRAY).forGoggles(tooltip);
        double fractDamage = damage / getMaxDamage();
        ChatFormatting formatting;
        if (fractDamage < 0.25) {
            formatting = ChatFormatting.GREEN;
        } else if (fractDamage < 0.5) {
            formatting = ChatFormatting.YELLOW;
        } else if (fractDamage < 0.75) {
            formatting = ChatFormatting.GOLD;
        } else {
            formatting = ChatFormatting.RED;
        }
        Lang.builder().space()
                .text(LangNumberFormat.format((int)(getMaxDamage() - damage)))
                    .style(formatting)
                .add(Lang.text(String.format(" / %s", (int)getMaxDamage()))
                    .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip);
        return true;
    }

    @Nullable
    public DrillCoreBE getCore() {
        if (level.getBlockEntity(getBlockPos().relative(
                    getBlockState().getValue(DrillHeadBlock.FACING).getOpposite())
                ) instanceof DrillCoreBE drillCore) {
            return drillCore;
        }
        return null;
    }

    @Override
    public void remove() {
        DrillCoreBE core = getCore();
        if (core != null) {
            core.removeDrillHead();
        }
        super.remove();
    }

    public static String DAMAGE_KEY = "Damage";

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        damage = compound.getDouble(DAMAGE_KEY);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putDouble(DAMAGE_KEY, damage);
    }
}
