package com.deepdrilling.mixin;

import com.deepdrilling.nodes.LootParser;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    // dealing with forge's networking api is a fate worse than death, mixins are much easier
    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true, remap = false)
    private void customPayload(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (packet.getIdentifier().equals(LootParser.PACKET_ID)) {
            LootParser.receiveFromServer(packet.getData());
            ci.cancel();
        }
    }
}
