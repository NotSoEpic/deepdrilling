package com.deepdrilling.forge.mixin;

import com.deepdrilling.fluid.SingleTank;
import com.deepdrilling.forge.FluidTankAssociationsImpl;
import com.deepdrilling.forge.fluid.ForgeSingleTank;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
@Mixin(CapabilityProvider.class)
public abstract class CapabilityProviderMixin {
    @Unique
    LazyOptional<ForgeSingleTank> optional;

    @Inject(method = "getCapability", at = @At("HEAD"), cancellable = true, remap = false)
    public <T> void checkLoaderTank(@NotNull Capability<T> cap, @Nullable Direction side, CallbackInfoReturnable<LazyOptional<T>> cir) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            CapabilityProvider self = (CapabilityProvider) (Object) this;

            if (optional != null) {
                cir.setReturnValue((LazyOptional<T>) optional);
                return;
            }

            if (self instanceof BlockEntity be) {
                Function<BlockEntity, SingleTank> fn = FluidTankAssociationsImpl.getters.get(be.getType());
                if (fn != null) {
                    optional = LazyOptional.of(() -> new ForgeSingleTank(fn.apply(be)));
                    optional.cast();
                    cir.setReturnValue((LazyOptional<T>) optional);
                }
            }
        }
    }
}
