package org.amateras_smp.amatweaks.mixins.hotbarRestock;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.util.ModIds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.amateras_smp.amatweaks.impl.util.container.AutoProcessableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(ModIds.itemscroller))
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
