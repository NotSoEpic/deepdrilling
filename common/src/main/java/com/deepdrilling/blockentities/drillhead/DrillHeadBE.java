package com.deepdrilling.blockentities.drillhead;

import com.deepdrilling.DrillHeadStats;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrillHeadBE extends KineticBlockEntity {
    private double damage = 0;
    private boolean unbreakable = false;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private int enchUnbreaking = 0;
    private int enchEfficiency = 0;
    private int enchFortune = 0;
    private boolean enchMending = false;

    public DrillHeadBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setDamage(double amount) {
        damage = amount;
        if (damage >= getMaxDamage()) {
            if (!unbreakable && !enchMending) {
                level.destroyBlock(getBlockPos(), false);
                return;
            } else {
                damage = getMaxDamage();
            }
        }
        setChanged();
        sendData();
    }

    public void applyDamage(double amount) {
        if (!unbreakable) {
            setDamage(damage + amount);
        }
    }

    public double getMaxDamage() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.DRILL_DURABILITY.getOrDefault(name, 100.0);
    }

    public boolean isEnchanted() {
        return enchUnbreaking > 0 || enchEfficiency > 0 || enchFortune > 0 || enchMending;
    }


    private boolean isFunctional() {
        if (enchMending) {
            return damage < getMaxDamage();
        }
        return true;
    }
    private static final Modifier<Boolean, DrillHeadBE> MODIFIER_FUNCTION = ModifierTypes.CAN_FUNCTION.create(
            ((core, head, be, base, prev) -> prev && be.isFunctional()), 0
    );

    private double getSpeedModifier() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        double speedStat = DrillHeadStats.DRILL_SPEED_MODIFIERS.getOrDefault(name, 1.0);
        return speedStat / (1 + (double) enchEfficiency / 5);
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_SPEED = ModifierTypes.SPEED.create(
            ((core, head, be, base, prev) -> prev * be.getSpeedModifier()), 1000
    );

    private double getDamageModifier() {
        return 1D / (1 + enchUnbreaking);
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_DAMAGE = ModifierTypes.DAMAGE.create(
            ((core, head, be, base, prev) -> prev * be.getDamageModifier()), -1000
    );

    private double getFortuneAmount() {
        return (double) enchFortune / 3;
    }
    private static final Modifier<Double, DrillHeadBE> MODIFIER_FORTUNE = ModifierTypes.FORTUNE.create(
            ((core, head, be, base, prev) -> prev + be.getFortuneAmount()), 1000
    );

    private DrillHeadStats.WeightMultipliers getWeightMultipliers() {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock());
        return DrillHeadStats.LOOT_WEIGHT_MULTIPLIER.getOrDefault(name, DrillHeadStats.WeightMultipliers.ONE);
    }
    private static final Modifier<DrillHeadStats.WeightMultipliers, DrillHeadBE> MODIFIER_WEIGHTS = ModifierTypes.RESOURCE_WEIGHT.create(
            ((core, head, be, base, prev) -> prev.mul(be.getWeightMultipliers())), 1000
    );

    public List<Modifier> getModifiers() {
        return List.of(MODIFIER_FUNCTION, MODIFIER_SPEED, MODIFIER_DAMAGE, MODIFIER_FORTUNE, MODIFIER_WEIGHTS);
    }

    public ItemStack writeItemNBT(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        tag.putInt("Damage", (int)damage);
        tag.putBoolean("Unbreakable", unbreakable);
        EnchantmentHelper.setEnchantments(enchantments, item);
        return item;
    }

    public void readItemNBT(ItemStack item) {
        CompoundTag nbt = item.getOrCreateTag();
        unbreakable = nbt.getBoolean("Unbreakable");
        enchantments = EnchantmentHelper.getEnchantments(item);
        updateCachedEnchantments();
        setDamage(nbt.getInt("Damage"));
    }

    private void updateCachedEnchantments() {
        enchUnbreaking = enchantments.getOrDefault(Enchantments.UNBREAKING, 0);
        enchEfficiency = enchantments.getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0);
        enchFortune = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
        enchMending = enchantments.getOrDefault(Enchantments.MENDING, 0) > 0;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if (!unbreakable) {
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
                    .text(LangNumberFormat.format((int) (getMaxDamage() - damage)))
                    .style(formatting)
                    .add(Lang.text(String.format(" / %s", (int) getMaxDamage()))
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip);
        }
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
    public static String UNBREAKABLE_KEY = "Unbreakable";
    public static String ENCHANTMENTS_KEY = "Enchantments";

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        damage = compound.getDouble(DAMAGE_KEY);
        unbreakable = compound.getBoolean(UNBREAKABLE_KEY);
        enchantments = EnchantmentHelper.deserializeEnchantments(compound.getList(ENCHANTMENTS_KEY, CompoundTag.TAG_COMPOUND));
        updateCachedEnchantments();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putDouble(DAMAGE_KEY, damage);
        compound.putBoolean(UNBREAKABLE_KEY, unbreakable);
        // thank you enchantment helper for being so helpful
        ListTag listTag = new ListTag();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                listTag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), entry.getValue()));
            }
        }
        compound.put(ENCHANTMENTS_KEY, listTag);
    }
}
