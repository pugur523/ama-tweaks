package org.amateras_smp.amatweaks.mixins.features.customCommandAlias;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.amateras_smp.amatweaks.InitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "onGameJoin", at = @At("HEAD"))
    private void onGameJoinRegisterCommands(GameJoinS2CPacket packet, CallbackInfo ci) {
        InitHandler.registerCommandOnGameJoin();
    }
}
