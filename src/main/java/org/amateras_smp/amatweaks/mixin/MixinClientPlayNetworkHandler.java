package org.amateras_smp.amatweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.amateras_smp.amatweaks.util.container.AutoProcessableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Inject(
            //#if MC >= 11600
            method = "onOpenScreen",
            //#else
            //$$ method = "onOpenContainer",
            //#endif
            at = @At("TAIL")
    )
    private void onOpenScreen(CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen != null) {
            ((AutoProcessableScreen) screen).setShouldProcess$AMT(true);
        }
    }
}
