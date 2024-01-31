package com.deepdrilling.forge.mixin;

import com.deepdrilling.blockentities.CollectorModuleBE;
import com.deepdrilling.blockentities.ModuleBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CollectorModuleBE.class)
public abstract class CollectorModuleBEMixin extends ModuleBE implements ICapabilityProvider, Container {
    public CollectorModuleBEMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (itemCapability == null) {
            // todo: should inject into <init>()V but i can not inject into any method for some reason
            itemCapability = LazyOptional.of(() -> new InvWrapper(this));
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemCapability.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }
}
