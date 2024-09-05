package com.deepdrilling.fabric.mixin;

import com.deepdrilling.DPartialModels;
import com.deepdrilling.fabric.DrillModFabric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrillModFabric.class)
public abstract class DrillModFabricMixin {
    @Inject(method = "Lcom/deepdrilling/fabric/DrillModFabric;onInitialize()V", at = @At("TAIL"), remap = false)
    private void loadPartialModelsClient(CallbackInfo ci) {
        DPartialModels.init();
    }
}
