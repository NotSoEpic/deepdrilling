package com.deepdrilling.blockentities;

import com.deepdrilling.blockentities.module.Modifier;
import com.deepdrilling.blockentities.module.ModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CollectorModuleBE extends ModuleBE implements Container {
    public final NonNullList<ItemStack> inventory;
    public CollectorModuleBE(BlockEntityType typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public List<ItemStack> collectItems(List<ItemStack> items) {
        items.forEach(stack -> {
            long leftover = insert(stack);
            stack.setCount((int) leftover);
        });
        items = items.stream().filter(stack -> !stack.isEmpty()).toList();
        return items;
    }

    private static final Modifier<List, CollectorModuleBE> ITEM_COLLECTION = ModifierTypes.OUTPUT_LIST.create(
            ((core, head, be, base, prev) -> be.collectItems(prev)), 0
    );
    private static final List<Modifier> MODIFIERS = List.of(ITEM_COLLECTION);
    @Override
    public List<Modifier> getModifiers() {
        return MODIFIERS;
    }

    @Override
    public boolean canPlayerUse(Player player) {
        return super.canPlayerUse(player) && !isEmpty();
    }

    public void givePlayerItems(Player player, Level level) {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack item = getItem(i);
            if (!player.addItem(item) && !item.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item.copy());
                itemEntity.setDeltaMovement(Vec3.ZERO);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
                setItem(i, ItemStack.EMPTY);
            }
        }
    }

    // puts item in, returning leftover count
    // why doesn't forge create have the cool transaction API :waaaaaa:
    public long insert(ItemStack stack) {
        stack = stack.copy();
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack inv = getItem(i);
            if (inv.isEmpty()) {
                setItem(i, stack);
                return 0;
            } else if (stack.is(inv.getItem())) {
                int delta = inv.getMaxStackSize() - inv.getCount();
                delta = Math.min(delta, stack.getCount());
                inv.setCount(inv.getCount() + delta);
                stack.setCount(stack.getCount() - delta);
            }
            if (stack.isEmpty()) {
                return 0;
            }
        }
        return stack.getCount();
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        ContainerHelper.saveAllItems(compound, inventory);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        ContainerHelper.loadAllItems(compound, inventory);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return getItem(slot).split(amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return inventory.remove(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }


    @Override
    public void clearContent() {
        inventory.clear();
    }
}
